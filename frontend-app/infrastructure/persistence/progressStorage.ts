import { ProgressData } from "../../types/localProgressData";

/**
 * Interface used to declare the contract of what any implementation of progress storage will have to do
 * Contains key methods such as saving progress, retrieving progress and clearing progress
 */
export interface ProgressStorage {
    getProgress(): Promise<ProgressData>;
    setProgress(data: ProgressData): Promise<void>;
    clearProgress(): Promise<void>;
}