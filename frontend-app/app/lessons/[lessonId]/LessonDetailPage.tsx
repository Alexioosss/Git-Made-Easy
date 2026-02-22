import { notFound } from "next/navigation";
import { TaskList } from "@/components/lessons/task-list";
import { LessonHeader } from "@/components/lessons/lesson-header";
import { GatewayFactory } from "@/config/GatewayFactory";
import { LessonProgress } from "@/types/taskProgress";

export default async function LessonDetailClient({ lessonId }: { lessonId: string }) {
    const lessonGateway = GatewayFactory.instance.lessonGateway;
    const lessonProgressGateway = GatewayFactory.instance.lessonProgressGateway;

    const lesson = await lessonGateway.getById(lessonId);
    if(!lesson) { notFound(); }

    const allLessons = await lessonGateway.getAll();
    const currentIndex = allLessons.findIndex(l => l.lessonId === lessonId);
    const nextLesson = allLessons[currentIndex + 1] ?? null;

    let progress: LessonProgress | undefined = undefined;
    try {
        progress = await lessonProgressGateway.getLessonProgress(lessonId);
    } catch {} // No lesson progress, ignore / continue

    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-4xl px-4 py-8 sm:py-12">
                <LessonHeader lesson={lesson} progress={progress} />
                <TaskList lesson={lesson} nextLesson={nextLesson} />
            </div>
        </div>
    );
}