export interface LocalTaskProgress {
    taskId: string;
    status: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED";
    attempts: number;
    lastInput: string;
    lastError: string;
    startedAt: string;
    completedAt?: string;
}

export interface LessonProgressData {
    lessonId: string;
    completedTasks: Record<string, LocalTaskProgress>;
}

export interface ProgressData {
    [lessonId: string]: LessonProgressData;
}