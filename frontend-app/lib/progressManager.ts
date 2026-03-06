import { LocalTaskProgress, ProgressData } from "@/infrastructure/persistence/localProgressData";
import { LocalStorageProgressStorage } from "@/infrastructure/persistence/localStorageProgressStorage";
import { ProgressStorage } from "@/infrastructure/persistence/progressStorage";
import { Lesson } from "@/types/lesson";

class ProgressManager {
    private static instance: ProgressManager;
    private storage: ProgressStorage;
    private progress: ProgressData = {};
    private initialized: boolean = false;

    private constructor() { this.storage = new LocalStorageProgressStorage(); }

    public static getInstance() {
        if(!ProgressManager.instance) { ProgressManager.instance = new ProgressManager(); }
        return ProgressManager.instance;
    }

    async init() {
        if (!this.initialized) {
        this.progress = await this.storage.getProgress();
        this.initialized = true;
        }
    }

    async getProgress() {
        if (!this.initialized) await this.init();
        return this.progress;
    }

    async updateLesson(lessonId: string, taskProgress: LocalTaskProgress) {
        if(!this.progress[lessonId]) {
            this.progress[lessonId] = {
                lessonId,
                completedTasks: {}
            };
        }
        this.progress[lessonId].completedTasks[taskProgress.taskId] = taskProgress;
        await this.storage.setProgress(this.progress);
    }

    async clearProgress() {
        await this.storage.clearProgress();
    }

    convertLocalToLessonProgress(localProgress: ProgressData, lessons: Lesson[]) {
        return Object.fromEntries(
            Object.values(localProgress).map(lp => {
                const lesson = lessons.find(l => l.lessonId === lp.lessonId);
                const totalTasksCount = lesson?.tasks?.length ?? 0;
                const completedTasksCount = Object.keys(lp.completedTasks).length;
                const currentTaskProgressId = Object.values(lp.completedTasks).at(-1)?.taskId ?? "";

                return [
                    lp.lessonId,
                    {
                        lessonProgressId: "",
                        userId: "",
                        lessonId: lp.lessonId,
                        currentTaskProgressId,
                        completedTasksCount,
                        totalTasksCount
                    }
                ];
            })
        );
    }
}

export default ProgressManager.getInstance();