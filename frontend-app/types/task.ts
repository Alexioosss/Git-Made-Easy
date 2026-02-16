export interface Task {
  taskId: string;
  title: string;
  description: string;
  difficulty: "EASY" | "MEDIUM" | "HARD";
  orderIndex: number;
  hint: string;
  expectedAnswer: string;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  difficulty: "EASY" | "MEDIUM" | "HARD";
  orderIndex: number;
  hint: string;
  expectedAnswer: string;
}