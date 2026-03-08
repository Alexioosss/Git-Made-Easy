import { LocalTaskProgress, ProgressData } from "@/types/localProgressData";
import { LocalStorageProgressStorage } from "@/infrastructure/persistence/localStorageProgressStorage";
import { ProgressStorage } from "@/infrastructure/persistence/progressStorage";
import { Lesson } from "@/types/lesson";
import { ProgressStatus } from "@/types/progressStatus";

class ProgressManager {
    private static instance: ProgressManager;
    private storage: ProgressStorage;
    private progress: ProgressData = {};
    private isProgressLoaded: boolean = false;

    private constructor() { this.storage = new LocalStorageProgressStorage(); }

    public static getInstance() {
        if(!ProgressManager.instance) { ProgressManager.instance = new ProgressManager(); }
        return ProgressManager.instance;
    }

    async getProgress() {
        if(!this.isProgressLoaded) {
            this.progress = await this.storage.getProgress();
            this.isProgressLoaded = true;
        }
        return this.progress;
    }

    async setProgress(progress: ProgressData) {
        this.progress = progress;
        this.isProgressLoaded = true;
        await this.storage.setProgress(progress);
    }

    async updateLesson(lessonId: string, taskProgress: LocalTaskProgress) {
        const current = await this.getProgress();
        if(!current[lessonId]) { current[lessonId] = { lessonId, tasks: {} } }
        current[lessonId].tasks[taskProgress.taskId] = taskProgress;
        await this.storage.setProgress(current);
    }

    async clearProgress() {
        await this.storage.clearProgress();
        this.progress = {};
        this.isProgressLoaded = false;
    }

    convertLocalToLessonProgress(localProgress: ProgressData, lessons: Lesson[]) {
        return Object.fromEntries(
            Object.values(localProgress).map(lp => {
                const lesson = lessons.find(l => l.lessonId === lp.lessonId);
                const totalTasksCount = lesson?.tasks?.length ?? 0;
                const completedTasksCount = Object.values(lp.tasks).filter(task => task.status === ProgressStatus.COMPLETED).length;
                const currentTaskProgressId = Object.values(lp.tasks).at(-1)?.taskId ?? "";
                return [ lp.lessonId, {
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