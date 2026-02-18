import Link from "next/link";
import { Lesson } from "@/types/lesson";
import { LessonProgress } from "@/types/taskProgress";
import { Progress } from "@/components/ui/progress";
import { ArrowLeft } from "lucide-react";

interface LessonHeaderProps {
  lesson: Lesson;
  progress?: LessonProgress;
}

export function LessonHeader({ lesson, progress }: LessonHeaderProps) {
  let progressPercentage: number | null = null;
  if(progress) { progressPercentage = progress?.totalTasksCount / progress?.totalTasksCount; }

  return (
    <div className="mb-8 sm:mb-10">
      <Link
        href="/lessons"
        className="mb-4 inline-flex items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground sm:mb-6"
      >
        <ArrowLeft className="h-3.5 w-3.5" />
        Back to Lessons
      </Link>

      <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:gap-4">
        <span className="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl bg-secondary text-base font-bold text-secondary-foreground sm:h-12 sm:w-12 sm:text-lg">
          {String(lesson.orderIndex).padStart(2, "0")}
        </span>
        <div className="flex-1">
          <h1 className="text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
            {lesson.title}
          </h1>
          <p className="mt-2 text-sm leading-relaxed text-muted-foreground sm:text-base">
            {lesson.description}
          </p>

          {progress && (
            <div className="mt-4 flex items-center gap-2 sm:gap-3">
              <Progress
                value={progressPercentage}
                className="h-2 flex-1 sm:max-w-xs"
              />
              <span className="shrink-0 text-sm font-medium text-foreground">
                {progressPercentage}%
              </span>
              <span className="hidden shrink-0 text-sm text-muted-foreground sm:inline">
                ({progress.completedTasksCount}/{progress.totalTasksCount} tasks)
              </span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}