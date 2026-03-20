import { LocalTaskProgress, ProgressData } from "@/types/localProgressData";
import { LocalStorageProgressStorage } from "@/infrastructure/persistence/localStorageProgressStorage";
import { ProgressStorage } from "@/infrastructure/persistence/progressStorage";
import { Lesson } from "@/types/lesson";
import { ProgressStatus } from "@/types/progressStatus";
import { LessonProgress } from "@/types/taskProgress";

/**
 * Singleton object to act as a centralised manager class for the user's progress, to allow for local and persisted progress syncing
 * Class makes use of the singleton object to ensure that only a single instance of the user's local progress can exist, avoiding repetition, data duplication, and conflicts between data and object instances
 */
class ProgressManager {
    private static instance: ProgressManager;
    private storage: ProgressStorage;
    private progress: ProgressData = {};
    private isProgressLoaded: boolean = false;

    private constructor() { this.storage = new LocalStorageProgressStorage(); } // Do not allow the creation of another instance of the user's local progress, which can lead to conflicts

    public static getInstance() {
        if(!ProgressManager.instance) { ProgressManager.instance = new ProgressManager(); } // If a singleton instance does not already exist, set one
        return ProgressManager.instance; // Return the instance of this singleton object for global reference / access
    }

    async getProgress() {
        if(!this.isProgressLoaded) {
            this.progress = await this.storage.getProgress(); // Get the storage from the storage method of choice, i.e. localStorage. Can be extended to implement further storage implementations, i.e. Session Storage, In-Memory etc.
            this.isProgressLoaded = true;
        }
        return this.progress;
    }

    async setProgress(progress: ProgressData) {
        this.progress = progress; // Sets the progress into the memory object
        this.isProgressLoaded = true;
        await this.storage.setProgress(progress); // Saves it to the method of storage, i.e. Local Storage
    }

    async updateLesson(lessonId: string, taskProgress: LocalTaskProgress) {
        const current = await this.getProgress(); // Fetches the current progress object
        if(!current[lessonId]) { current[lessonId] = { lessonId, tasks: {} } } // If the progress to be updated does nto exist, create it
        current[lessonId].tasks[taskProgress.taskId] = taskProgress; // Update its tasks' progress with the user's progress for those tasks
        await this.storage.setProgress(current); // Save the updated user progress
    }

    async clearProgress() {
        await this.storage.clearProgress(); // Clear the object from the method of storage, i.e. Local Storage
        this.progress = {}; // Set the memory reference of the progress object to an empty object
        this.isProgressLoaded = false;
    }

    /**
     * 
     * @param localProgress The user's local progress object, collected after the user completes a task
     * @param lessons The list of lessons available, used to match a lesson with its tasks, and a task with the user's progress
     * @returns Returns the a list of lesson progress for each lesson available, based on the current user's progress
     */
    convertLocalToLessonProgress(localProgress: ProgressData, lessons: Lesson[]): Record<string, LessonProgress> {
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