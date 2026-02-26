import { TaskList } from "@/components/lessons/task-list";
import { LessonHeader } from "@/components/lessons/lesson-header";
import { LessonProgress } from "@/types/taskProgress";
import { Lesson } from "@/types/lesson";

interface LessonDetailProps {
    lesson: Lesson;
    progress?: LessonProgress;
    nextLesson: Lesson | null;
}

export default function LessonDetail({ lesson, progress, nextLesson }: LessonDetailProps) {
    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-4xl px-4 py-8 sm:py-12">
                <LessonHeader lesson={lesson} progress={progress} />
                <TaskList lesson={lesson} nextLesson={nextLesson} />
            </div>
        </div>
    );
}