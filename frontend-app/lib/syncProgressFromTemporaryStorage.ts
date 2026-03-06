import { GatewayFactory } from "@/config/GatewayFactory";
import progressManager from "./progressManager";
import { ProgressData } from "@/infrastructure/persistence/localProgressData";

export async function syncProgressFromTemporaryStorage() {
    const localProgress: ProgressData = await progressManager.getProgress();
    if(localProgress) {
        for (const lessonId in localProgress) {
            const lesson = localProgress[lessonId];

            const taskUpdates = Object.values(lesson.tasks).map((task) => ({
                taskId: task.taskId,
                status: task.status,
                attempts: task.attempts,
                lastInput: task.lastInput,
                lastError: task.lastError,
                startedAt: task.startedAt,
                completedAt: task.completedAt,
            }));
            await GatewayFactory.instance.taskProgressGateway.syncLocalProgress(lessonId, taskUpdates)
        }
        await progressManager.clearProgress();
    } else { return; }
}