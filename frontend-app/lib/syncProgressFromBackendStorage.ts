import { GatewayFactory } from "@/config/GatewayFactory";
import progressManager from "@/context/progressManager";
import { getCurrentUser } from "./auth";

export async function syncProgressFromBackendStorage() {
    const user = await getCurrentUser();
    if(!user) { return; }
    
    const backendProgress = await GatewayFactory.instance.taskProgressGateway.getAllTaskProgress();
    if(!backendProgress || backendProgress.length == 0) { return; }

    const localProgress: any = {};
    for(const taskProgress of backendProgress) {
        const lessonId = taskProgress.lessonId;
        if(!localProgress[lessonId]) {
            localProgress[lessonId] = {
                lessonId,
                tasks: {}
            };
        }
        localProgress[lessonId].tasks[taskProgress.taskId] = {
            taskId: taskProgress.taskId,
            status: taskProgress.status,
            attempts: taskProgress.attempts,
            lastInput: taskProgress.lastInput,
            lastError: taskProgress.lastError,
            startedAt: taskProgress.startedAt,
            completedAt: taskProgress.completedAt
        };
    }
    await progressManager.setProgress(localProgress);
}