import { ProgressData } from "./localProgressData";

export interface ProgressStorage {
    getProgress(): Promise<ProgressData>;
    setProgress(data: ProgressData): Promise<void>;
    clearProgress(): Promise<void>;
}