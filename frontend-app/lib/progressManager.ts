import { LocalTaskProgress, ProgressData } from "@/infrastructure/persistence/localProgressData";
import { LocalStorageProgressStorage } from "@/infrastructure/persistence/localStorageProgressStorage";
import { ProgressStorage } from "@/infrastructure/persistence/progressStorage";
import { Lesson } from "@/types/lesson";
import { ProgressStatus } from "@/types/progressStatus";

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
        if(!this.initialized) {
            this.progress = await this.storage.getProgress();
            this.initialized = true;
        }
    }

    async getProgress() {
        const reloadedProgress = await this.storage.getProgress();
        this.progress = reloadedProgress;
        return reloadedProgress;
    }

    async updateLesson(lessonId: string, taskProgress: LocalTaskProgress) {
        const current = await this.getProgress();
        if(!current[lessonId]) { current[lessonId] = { lessonId, tasks: {} } }
        current[lessonId].tasks[taskProgress.taskId] = taskProgress;
        await this.storage.setProgress(current);
        this.progress = current;
    }

    async clearProgress() {
        await this.storage.clearProgress();
        this.progress = {};
        this.initialized = false;
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