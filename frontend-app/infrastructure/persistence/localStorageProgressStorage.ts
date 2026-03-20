import { LocalTaskProgress, ProgressData } from "../../types/localProgressData";
import { ProgressStorage } from "./progressStorage";

/**
 * Implementation of the progress storage interface which uses the localStorage as method of local storage for when the user is offline, or for when the server is not responding
 */
export class LocalStorageProgressStorage implements ProgressStorage {
    private readonly key = "user_progress"; // Key used to represent the user's local storage object with their progress made so far

    /**
     * Fetches the user's local progress made, found in the local storage, accessible via the key above
     * @returns Returns the local progress object converted into the domain entity ProgressData
     */
    async getProgress(): Promise<ProgressData> {
        const json = localStorage.getItem(this.key);
        if(!json) { return {} };
        try {
            const rawObject = JSON.parse(json);
            const progress: ProgressData = {};
            for(const lessonId in rawObject) {
                const lessonData = rawObject[lessonId];
                const tasks: Record<string, LocalTaskProgress> = {};
                if(lessonData.tasks) {
                    for(const taskId in lessonData.tasks) {
                        tasks[taskId] = lessonData.tasks[taskId];
                    }
                }
                progress[lessonId] = { lessonId, tasks: tasks };
            }
            return progress;
        } catch { return {}; }
    }

    /**
     * Function to save the user's progress made so far locally
     * @param progress The progress the user made that requires saving to local storage for permanent persistence
     */
    async setProgress(progress: ProgressData): Promise<void> {
        const json = JSON.stringify(progress, (key, value) => {
            if(value instanceof Set) { return Array.from(value); }
            return value;
        });
        localStorage.setItem(this.key, json);
    }

    /**
     * Function to clear / remove the local progress that the user made so far, effectively emptying the local storage's user progress object
     */
    async clearProgress(): Promise<void> {
        localStorage.removeItem(this.key);
    }
}