import { LocalTaskProgress, ProgressData } from "./localProgressData";
import { ProgressStorage } from "./progressStorage";

export class LocalStorageProgressStorage implements ProgressStorage {
    private readonly key = "user_progress";

    async getProgress(): Promise<ProgressData> {
        const json = localStorage.getItem(this.key);
        if(!json) return {};
        try {
            const rawObject = JSON.parse(json);
            const progress: ProgressData = {};
            for(const lessonId in rawObject) {
                const lessonData = rawObject[lessonId];
                const completedTasks: Record<string, LocalTaskProgress> = {};
                if(lessonData.completedTasks) {
                    for(const taskId in lessonData.completedTasks) {
                        completedTasks[taskId] = lessonData.completedTasks[taskId];
                    }
                }
                progress[lessonId] = { lessonId, completedTasks };
            }
            return progress;
        } catch { return {}; }
    }

    async setProgress(progress: ProgressData): Promise<void> {
        const json = JSON.stringify(progress, (key, value) => {
            if(value instanceof Set) {
                return Array.from(value);
            }
            return value;
        });
        localStorage.setItem(this.key, json);
    }

    async clearProgress(): Promise<void> {
        localStorage.removeItem(this.key);
    }
}