import { DifficultyLevels } from "./difficultyLevels";

export interface Task {
  taskId: string;
  lessonId: string;
  title: string;
  content: string;
  expectedCommand: string;
  hint: string;
  taskOrder: number;
  difficulty: DifficultyLevels;
}