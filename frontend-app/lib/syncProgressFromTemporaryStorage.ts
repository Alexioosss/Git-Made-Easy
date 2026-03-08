import { GatewayFactory } from "@/config/GatewayFactory";
import progressManager from "../context/progressManager";
import { ProgressData } from "@/types/localProgressData";
import { safeCallWrapper } from "./safeCallWrapper";

export async function syncProgressFromTemporaryStorage() {
    const localProgress: ProgressData = await progressManager.getProgress();
    if(!localProgress) { return; }
    if(localProgress) {
        for(const lessonId in localProgress) {
            const lesson = localProgress[lessonId];
            for(const taskId in lesson.tasks) {
                if(!taskId || taskId === "undefined") {
                    delete lesson.tasks[taskId];
                }
            }

            const taskUpdates = Object.values(lesson.tasks).filter(task => task.taskId && task.taskId !== "undefined")
            .map((task) => ({
                taskId: task.taskId,
                status: task.status,
                attempts: task.attempts,
                lastInput: task.lastInput,
                lastError: task.lastError,
                startedAt: task.startedAt,
                completedAt: task.completedAt,
            }));
            if(taskUpdates.length === 0) { continue; }
            await safeCallWrapper(() => GatewayFactory.instance.taskProgressGateway.syncLocalProgress(lessonId, taskUpdates))
        }
        await progressManager.setProgress(localProgress);
    } else { return; }
}