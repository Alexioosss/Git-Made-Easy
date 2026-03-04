import Link from "next/link";
import { Lesson } from "@/types/lesson";
import { LessonProgress } from "@/types/taskProgress";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { ArrowRight, CheckCircle2 } from "lucide-react";
import { DifficultyLevels } from "@/types/difficultyLevels";

interface LessonCardProps {
  lesson: Lesson;
  progress?: LessonProgress;
}

export function LessonCard({ lesson, progress }: LessonCardProps) {
  const difficultyDistribution = (lesson.tasks ?? []).reduce((acc, task) => { acc[task.difficulty] = (acc[task.difficulty] || 0) + 1; return acc; }, {} as Record<string, number>);

  let progressPercentage = null;
  if(progress) { progressPercentage = (progress.completedTasksCount / progress.totalTasksCount) * 100; }
  const isComplete = progress && progressPercentage === 100;

  return (
    <Link href={`/lessons/${lesson.lessonId}`} title={`Lesson ${lesson.lessonOrder} - ${lesson.title}`}
      className="group flex flex-col rounded-xl border border-border bg-card transition-all
      hover:scale-[1.02] hover:border-primary/80 hover:shadow-xl hover:shadow-primary/20 md:flex-row md:items-center">
      {/* Order number */}
      <div className="flex items-center gap-4 border-b border-border px-5 py-4 md:justify-center md:gap-0 md:border-b-0 md:border-r md:px-8 md:py-10">
        <span className="text-2xl font-bold text-muted-foreground/40 group-hover:text-primary/100 transition-colors md:text-3xl">
          {String(lesson.lessonOrder).padStart(2, "0")}
        </span>
        {/* Title shown inline on mobile only */}
        <h3 className="text-base font-semibold text-card-foreground group-hover:text-primary transition-colors md:hidden">
          {lesson.title}
        </h3>
        {isComplete && (
          <CheckCircle2 className="ml-auto h-5 w-5 text-primary md:hidden" />
        )}
      </div>

      {/* Content */}
      <div className="flex flex-1 flex-col gap-3 p-5 sm:p-6">
        <div className="hidden items-center gap-3 md:flex">
          <h3 className="text-lg font-semibold text-card-foreground group-hover:text-primary transition-colors">
            {lesson.title}
          </h3>
          {isComplete && <CheckCircle2 className="h-5 w-5 text-primary" />}
        </div>

        <p className="text-sm leading-relaxed text-muted-foreground">
          {lesson.description}
        </p>

        <div className="flex flex-wrap items-center gap-2">
          <span className="text-xs text-muted-foreground">
            {lesson.taskIds.length} tasks
          </span>
          <span className="text-muted-foreground/30">|</span>
          {Object.entries(difficultyDistribution).map(
            ([difficulty, count]) => (
              <Badge key={difficulty} variant={difficulty.toLowerCase() as DifficultyLevels} className={`text-xs`}>
                {count} {difficulty.toLowerCase()}
              </Badge>
            )
          )}
        </div>

        {/* Progress bar if logged in */}
        {progress && (
          <div className="mt-1 flex items-center gap-2 sm:gap-3">
            <Progress value={progressPercentage} className="h-1.5 flex-1"/>
            <span className="shrink-0 text-xs font-medium text-muted-foreground">
              {progressPercentage}%
            </span>
            <span className="hidden shrink-0 text-xs text-muted-foreground/60 sm:inline">
              ({progress.completedTasksCount}/{progress.totalTasksCount} tasks)
            </span>
          </div>
        )}
      </div>

      {/* Arrow */}
      <div className="hidden items-center px-6 md:flex">
        <ArrowRight className="h-5 w-5 text-muted-foreground/40 transition-all group-hover:translate-x-1 group-hover:text-primary" />
      </div>
    </Link>
  );
}