import { LocalTaskProgress, ProgressData } from "../../types/localProgressData";
import { ProgressStorage } from "./progressStorage";

export class LocalStorageProgressStorage implements ProgressStorage {
    private readonly key = "user_progress"; // Key used to represent user's local storage object with progress made so far

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

    async setProgress(progress: ProgressData): Promise<void> {
        const json = JSON.stringify(progress, (key, value) => {
            if(value instanceof Set) { return Array.from(value); }
            return value;
        });
        localStorage.setItem(this.key, json);
    }

    async clearProgress(): Promise<void> {
        localStorage.removeItem(this.key);
    }
}