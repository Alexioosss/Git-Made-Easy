export interface TaskAttemptRequest {
  answer: string;
}

export interface TaskProgress {
  userId: string;
  lessonId: string;
  taskId: string;
  completed: boolean;
  attempts: number;
  lastAttemptDate: string;
}

export interface LessonProgress {
  userId: string;
  lessonId: string;
  completedTasks: number;
  totalTasks: number;
  progressPercentage: number;
}