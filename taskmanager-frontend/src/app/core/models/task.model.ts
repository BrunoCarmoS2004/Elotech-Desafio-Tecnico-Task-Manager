export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface Task {
  id: string;
  title: string;
  description: string;
  status: TaskStatus;
  priority: TaskPriority;
  deadline: string;
  responsibleId: string;
  projectId: string;
}

export interface TaskLog {
  id: string;
  taskId: string;
  userId: string;
  alteredField: string;
  oldValue: string;
  newValue: string;
  alteredDate: string;
}
