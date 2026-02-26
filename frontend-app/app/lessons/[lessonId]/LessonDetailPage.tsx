import { notFound } from "next/navigation";
import { TaskList } from "@/components/lessons/task-list";
import { LessonHeader } from "@/components/lessons/lesson-header";
import { GatewayFactory } from "@/config/GatewayFactory";
import { LessonProgress } from "@/types/taskProgress";
import { safeCallWrapper } from "@/lib/safeCallWrapper";
import { Lesson } from "@/types/lesson";

export default async function LessonDetailClient({ lessonId }: { lessonId: string }) {
    const lessonGateway = GatewayFactory.instance.lessonGateway;
    const lessonProgressGateway = GatewayFactory.instance.lessonProgressGateway;

    const lessonResponse = await safeCallWrapper(lessonGateway.getById(lessonId));
    if(!lessonResponse.ok || !lessonResponse.data) {
        return (
            <section className="border-t border-border px-4 py-60 sm:py-60">
                <div className="mx-auto max-w-2xl text-center">
                    <h2 className="text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
                        Unable to load the lesson
                    </h2>
                    <p className="mt-2 text-md text-muted-foreground">
                        The lesson you're looking for is not available right now. Please try again later.
                    </p>
                </div>
            </section>
        );
    }
    
    const lesson = (!lessonResponse.ok || !lessonResponse.data) ? notFound() : lessonResponse.data;

    const allLessonsResponse = await safeCallWrapper(lessonGateway.getAll());
    const allLessons = (!allLessonsResponse.ok || !allLessonsResponse.data) ? [] : allLessonsResponse.data;

    const currentIndex = allLessons.findIndex(l => l.lessonId === lessonId);
    const nextLesson = allLessons[currentIndex + 1] ?? null;

    let progress: LessonProgress | undefined = undefined;
    try { progress = await lessonProgressGateway.getLessonProgress(lessonId); }
    catch {} // No lesson progress, ignore / continue
    
    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-4xl px-4 py-8 sm:py-12">
                <LessonHeader lesson={lesson} progress={progress} />
                <TaskList lesson={lesson} nextLesson={nextLesson} />
            </div>
        </div>
    );
}