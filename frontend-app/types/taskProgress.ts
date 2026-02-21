export interface TaskAttemptRequest {
  answer: string;
}

export interface TaskProgress {
  taskProgressId: string;
  userId: string;
  taskId: string;
  lessonId: string;
  taskTitle: string;
  status: string;
  attempts: number;
  lastInput: string;
  lastError: string;
  startedAt: string;
  completedAt: string;
}

export interface LessonProgress {
  lessonProgressId: string;
  userId: string;
  lessonId: string;
  currentTaskProgressId: string;
  completedTasksCount: number;
  totalTasksCount: number;
}