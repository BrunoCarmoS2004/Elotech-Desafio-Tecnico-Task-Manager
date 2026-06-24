// src/app/services/task.service.ts
import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Task, TaskStatus, TaskPriority, TaskLog } from '../models/task.model';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, PaginatedResponse } from '../models/response.model';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.apiUrl;

  tasks = signal<Task[]>([]);

  createTask(payload: any): Observable<ApiResponse<Task>> {
    return this.http
      .post<ApiResponse<Task>>(`${this.API_URL}/task`, payload)
      .pipe(
        tap((res) => {
          this.tasks.update((prev) => [...prev, res.data]);
        }),
      );
  }

  loadTasksByFilters(projectId: string): void {
    this.http
      .post<
        PaginatedResponse<Task>
      >(`${this.API_URL}/task/filters`, { projectId })
      .subscribe((res) => this.tasks.set(res.content));
  }

  getTaskById(taskId: string): Observable<ApiResponse<Task>> {
    return this.http.get<ApiResponse<Task>>(`${this.API_URL}/task/${taskId}`);
  }

  updateTask(taskId: string, payload: any): Observable<ApiResponse<Task>> {
    return this.http
      .put<ApiResponse<Task>>(`${this.API_URL}/task/${taskId}`, payload)
      .pipe(
        tap((res) => {
          this.tasks.update((prev) =>
            prev.map((t) => (t.id === taskId ? res.data : t)),
          );
        }),
      );
  }

  // 3. Mudar Status via Drag and Drop (PATCH /task/change/{id}/status)
  updateTaskStatus(
    taskId: string,
    status: TaskStatus,
  ): Observable<ApiResponse<string>> {
    return this.http
      .patch<
        ApiResponse<string>
      >(`${this.API_URL}/task/change/${taskId}/status`, null, { params: { status } })
      .pipe(
        tap(() => {
          this.tasks.update((prev) =>
            prev.map((t) => (t.id === taskId ? { ...t, status } : t)),
          );
        }),
      );
  }

  // 7. Mudar Prioridade isoladamente (PATCH /task/change/{id}/priority)
  updateTaskPriority(
    taskId: string,
    priority: TaskPriority,
  ): Observable<ApiResponse<string>> {
    return this.http
      .patch<
        ApiResponse<string>
      >(`${this.API_URL}/task/change/${taskId}/priority`, null, { params: { priority } })
      .pipe(
        tap(() => {
          this.tasks.update((prev) =>
            prev.map((t) => (t.id === taskId ? { ...t, priority } : t)),
          );
        }),
      );
  }

  // 8. Mudar Responsável isoladamente (PATCH /task/change/{id}/responsible/{responsibleId})
  updateTaskResponsible(
    taskId: string,
    responsibleId: string,
  ): Observable<ApiResponse<string>> {
    return this.http
      .patch<
        ApiResponse<string>
      >(`${this.API_URL}/task/change/${taskId}/responsible/${responsibleId}`, null)
      .pipe(
        tap(() => {
          this.tasks.update((prev) =>
            prev.map((t) => (t.id === taskId ? { ...t, responsibleId } : t)),
          );
        }),
      );
  }

  getTaskLogs(
    taskId: string,
    page: number = 0,
    size: number = 20,
  ): Observable<PaginatedResponse<TaskLog>> {
    return this.http.get<PaginatedResponse<TaskLog>>(
      `${this.API_URL}/log/task/${taskId}`,
      {
        params: { page: page.toString(), size: size.toString() },
      },
    );
  }

  // Buscar TODAS as tarefas do sistema (Exclusivo ADMIN - GET /task)
  getAllTasks(
    page: number = 0,
    size: number = 20,
  ): Observable<PaginatedResponse<Task>> {
    return this.http.get<PaginatedResponse<Task>>(`${this.API_URL}/task`, {
      params: { page: page.toString(), size: size.toString() },
    });
  }
}
