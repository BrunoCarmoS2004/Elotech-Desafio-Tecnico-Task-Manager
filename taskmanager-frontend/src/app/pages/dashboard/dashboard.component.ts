import { Component, inject, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  DragDropModule,
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { AuthService } from '../../core/services/auth.service';
import {
  Task,
  TaskStatus,
  TaskPriority,
  TaskLog,
} from '../../core/models/task.model';
import { ProjectService } from '../../core/services/project.service';
import { TaskService } from '../../core/services/task.service';
import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { User } from '../../core/models/auth.model';
import { UserService } from '../../core/services/user.service';
import { ProjectReport } from '../../core/models/project.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, DragDropModule, ReactiveFormsModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  authService = inject(AuthService);
  projectService = inject(ProjectService);
  taskService = inject(TaskService);
  userService = inject(UserService);
  private fb = inject(FormBuilder);

  minDate: string = new Date().toISOString().split('T')[0];
  pageTaskLogSize = 5;
  pageGetAllAdminViewSize = 5;

  showCreateForm = signal<boolean>(false);
  showTaskForm = signal<boolean>(false);
  showMemberForm = signal<boolean>(false);
  showReportModal = signal<boolean>(false);
  editingTask = signal<Task | null>(null);

  projectReport = signal<ProjectReport | null>(null);

  selectedTaskForLogs = signal<Task | null>(null);
  taskLogs = signal<TaskLog[]>([]);
  logCurrentPage = signal<number>(0);
  logTotalPages = signal<number>(0);

  adminViewType = signal<'USERS' | 'PROJECTS' | 'TASKS' | null>(null);
  adminPaginatedData = signal<any[]>([]);
  currentPage = signal<number>(0);
  totalPages = signal<number>(0);

  searchEmail = signal<string>('');
  foundUser = signal<User | null>(null);
  memberError = signal<string | null>(null);
  isSearching = signal<boolean>(false);

  projectForm = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    description: ['', [Validators.required]],
  });

  taskForm = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(4)]],
    description: ['', [Validators.required]],
    priority: ['LOW' as TaskPriority, [Validators.required]],
    deadline: ['', [Validators.required]],
    responsibleId: [''],
  });

  userRole = computed(() => this.authService.userRole() ?? 'MEMBER');
  userId = computed(() => this.authService.userId());
  isMemberOnly = computed(() => this.userRole() === 'MEMBER');
  isAdmin = computed(() => this.userRole() === 'ADMIN');

  canCreateProject = computed(
    () => this.userRole() === 'ADMIN' || this.userRole() === 'MANAGER',
  );

  canManageTasks = computed(() => {
    const role = this.userRole();
    return (
      role === 'ADMIN' ||
      role === 'MANAGER' ||
      role === 'MEMBER_MANAGER' ||
      role == 'MEMBER'
    );
  });

  hasNoProjects = computed(
    () => this.projectService.userProjects().length === 0,
  );

  todoTasks = computed(() =>
    this.taskService.tasks().filter((t) => t.status === 'TODO'),
  );
  inProgressTasks = computed(() =>
    this.taskService.tasks().filter((t) => t.status === 'IN_PROGRESS'),
  );
  doneTasks = computed(() =>
    this.taskService.tasks().filter((t) => t.status === 'DONE'),
  );

  ngOnInit(): void {
    this.loadInitialData();
  }

  logout(): void {
    this.authService.logout();
  }

  loadInitialData(): void {
    const currentUserId = this.userId();
    if (currentUserId) {
      this.projectService.getProjectsByMember(currentUserId).subscribe(() => {
        const currentProj = this.projectService.currentProject();
        if (currentProj) {
          this.taskService.loadTasksByFilters(currentProj.id);
        }
      });
    }
  }

  public closeAllForms(): void {
    this.showCreateForm.set(false);
    this.showTaskForm.set(false);
    this.showReportModal.set(false);
    this.showMemberForm.set(false);
    this.adminViewType.set(null);
    this.editingTask.set(null);
    this.selectedTaskForLogs.set(null);

    this.taskLogs.set([]);
    this.logCurrentPage.set(0);
    this.logTotalPages.set(0);

    this.projectForm.reset();
    this.taskForm.reset({ priority: 'LOW' });
    this.searchEmail.set('');
    this.foundUser.set(null);
    this.memberError.set(null);
  }

  toggleCreateForm(): void {
    const currentStatus = this.showCreateForm();
    this.closeAllForms();
    this.showCreateForm.set(!currentStatus);
  }

  toggleTaskForm(): void {
    const currentStatus = this.showTaskForm();
    this.closeAllForms();
    if (!currentStatus) {
      this.showTaskForm.set(true);
    }
  }

  toggleMemberForm(): void {
    const currentStatus = this.showMemberForm();
    this.closeAllForms();
    this.showMemberForm.set(!currentStatus);
  }

  onSearchMember(): void {
    const email = this.searchEmail().trim();
    if (!email) return;
    this.isSearching.set(true);
    this.memberError.set(null);
    this.foundUser.set(null);

    this.userService.getUserByEmail(email).subscribe({
      next: (res) => {
        this.foundUser.set(res.data);
        this.isSearching.set(false);
      },
      error: () => {
        this.memberError.set('Usuário não encontrado');
        this.isSearching.set(false);
      },
    });
  }

  onAddMember(userToAdd: User): void {
    const currentProj = this.projectService.currentProject();
    if (!currentProj) return;

    this.projectService
      .addProjectMember({
        projectId: currentProj.id,
        memberIds: [userToAdd.id],
      })
      .subscribe({
        next: () => {
          alert(`Membro adicionado!`);
          this.closeAllForms();
        },
      });
  }

  onProjectChange(projectId: string): void {
    const selectedProj = this.projectService
      .userProjects()
      .find((p) => p.id === projectId);
    if (selectedProj) {
      this.projectReport.set(null);
      this.projectService.currentProject.set(selectedProj);
      this.taskService.loadTasksByFilters(projectId);
      this.closeAllForms();
    }
  }

  onSubmitProject(): void {
    if (this.projectForm.invalid || !this.userId()) return;
    const formValues = this.projectForm.getRawValue();
    this.projectService
      .createProject({
        ...formValues,
        creatorId: this.userId()!,
        members: [this.userId()!],
      })
      .subscribe({
        next: (res) => {
          this.showCreateForm.set(false);
          this.taskService.loadTasksByFilters(res.data.id);
        },
      });
  }

  generateReport(): void {
    const currentProj = this.projectService.currentProject();
    if (!currentProj) return;

    this.projectService.getProjectReport(currentProj.id).subscribe({
      next: (res) => {
        this.projectReport.set(res.data);
        this.showReportModal.set(true);
      },
      error: (err) =>
        console.error('Erro ao buscar o relatório do projeto:', err),
    });
  }

  closeReportModal(): void {
    this.showReportModal.set(false);
    this.projectReport.set(null);
  }

  openTaskLogs(task: Task, page: number = 0): void {
    this.selectedTaskForLogs.set(task);
    this.logCurrentPage.set(page);

    this.taskService
      .getTaskLogs(task.id, page, this.pageTaskLogSize)
      .subscribe({
        next: (res) => this.handleLogResponse(res),
        error: (err) => console.error('Erro ao buscar logs da tarefa:', err),
      });
  }

  changeLogPage(step: number): void {
    const nextLogPage = this.logCurrentPage() + step;
    const currentTask = this.selectedTaskForLogs();

    if (currentTask && nextLogPage >= 0 && nextLogPage < this.logTotalPages()) {
      this.openTaskLogs(currentTask, nextLogPage);
    }
  }

  closeTaskLogs(): void {
    this.closeAllForms();
  }

  openEditModal(task: Task, event: Event): void {
    event.stopPropagation();
    this.closeAllForms();
    this.editingTask.set(task);
    this.showTaskForm.set(true);
    this.taskForm.setValue({
      title: task.title,
      description: task.description,
      priority: task.priority,
      deadline: task.deadline ? task.deadline.split('T')[0] : '',
      responsibleId: task.responsibleId || '',
    });
  }

  onSubmitTask(): void {
    const currentProj = this.projectService.currentProject();
    if (this.taskForm.invalid || !currentProj) return;

    const formValues = this.taskForm.getRawValue();
    const taskData = {
      ...formValues,
      deadline: `${formValues.deadline}T23:59:59`,
      projectId: currentProj.id,
      responsibleId: formValues.responsibleId || this.userId()!,
    };

    if (this.editingTask()) {
      this.taskService
        .updateTask(this.editingTask()!.id, {
          ...taskData,
          status: this.editingTask()!.status,
        })
        .subscribe({ next: () => this.closeTaskModal() });
    } else {
      this.taskService
        .createTask({ ...taskData, status: 'TODO' })
        .subscribe({ next: () => this.toggleTaskForm() });
    }
  }

  closeTaskModal(): void {
    this.closeAllForms();
  }

  loadAdminView(type: 'USERS' | 'PROJECTS' | 'TASKS', page: number = 0): void {
    this.closeAllForms();
    this.adminViewType.set(type);
    this.currentPage.set(page);

    if (type === 'USERS') {
      this.userService
        .getAllUsers(page, this.pageGetAllAdminViewSize)
        .subscribe((res) => this.handleAdminResponse(res));
    } else if (type === 'PROJECTS') {
      this.projectService
        .getAllProjects(page, this.pageGetAllAdminViewSize)
        .subscribe((res) => this.handleAdminResponse(res));
    } else if (type === 'TASKS') {
      this.taskService
        .getAllTasks(page, this.pageGetAllAdminViewSize)
        .subscribe((res) => this.handleAdminResponse(res));
    }
  }

  changeAdminPage(step: number): void {
    const nextPage = this.currentPage() + step;
    if (nextPage >= 0 && nextPage < this.totalPages()) {
      this.loadAdminView(this.adminViewType()!, nextPage);
    }
  }

  closeAdminView(): void {
    this.closeAllForms();
  }

  drop(event: CdkDragDrop<Task[]>, targetStatus: TaskStatus): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    } else {
      const task = event.previousContainer.data[event.previousIndex];
      task.status = targetStatus;

      this.taskService.updateTaskStatus(task.id, targetStatus).subscribe({
        error: () => {
          const current = this.projectService.currentProject();
          if (current) this.taskService.loadTasksByFilters(current.id);
        },
      });

      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    }
  }

  private handleLogResponse(res: any): void {
    this.taskLogs.set(res.content);
    this.logTotalPages.set(res.page?.totalPages ?? 0);
  }

  private handleAdminResponse(res: any): void {
    this.adminPaginatedData.set(res.content);
    this.totalPages.set(res.page.totalPages);
  }
}
