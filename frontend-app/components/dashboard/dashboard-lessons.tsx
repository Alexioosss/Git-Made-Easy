import Link from "next/link";
import { mockLessons, mockLessonProgress } from "@/lib/mock-data";
import { Progress } from "@/components/ui/progress";
import { ArrowRight, CheckCircle2 } from "lucide-react";

export function DashboardLessons() {
  return (
    <div>
      <h2 className="mb-4 text-lg font-semibold text-foreground">
        Your Lessons
      </h2>
      <div className="flex flex-col gap-3">
        {mockLessons.map((lesson) => {
          const progress = mockLessonProgress[lesson.lessonId];
          const isComplete = progress && progress.progressPercentage === 100;
          const hasStarted = !!progress;

          return (
            <Link
              key={lesson.lessonId}
              href={`/lessons/${lesson.lessonId}`}
              className="group flex items-center gap-3 rounded-xl border border-border bg-card p-3.5 transition-all hover:border-primary/30 sm:gap-4 sm:p-4"
            >
              <div
                className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg sm:h-10 sm:w-10 ${
                  isComplete
                    ? "bg-primary/20"
                    : hasStarted
                      ? "bg-secondary"
                      : "bg-secondary"
                }`}
              >
                {isComplete ? (
                  <CheckCircle2 className="h-4 w-4 text-primary sm:h-5 sm:w-5" />
                ) : (
                  <span className="text-xs font-semibold text-secondary-foreground sm:text-sm">
                    {String(lesson.orderIndex).padStart(2, "0")}
                  </span>
                )}
              </div>

              <div className="min-w-0 flex-1">
                <div className="flex items-center justify-between gap-2">
                  <h3 className="truncate text-sm font-medium text-card-foreground group-hover:text-primary transition-colors">
                    {lesson.title}
                  </h3>
                  {progress && (
                    <span className="shrink-0 text-sm font-semibold text-foreground">
                      {progress.progressPercentage}%
                    </span>
                  )}
                </div>
                {progress ? (
                  <div className="mt-2 flex items-center gap-2 sm:gap-3">
                    <Progress
                      value={progress.progressPercentage}
                      className="h-1.5 flex-1"
                    />
                    <span className="shrink-0 text-xs text-muted-foreground">
                      {progress.completedTasks}/{progress.totalTasks}
                    </span>
                  </div>
                ) : (
                  <p className="mt-1 text-xs text-muted-foreground">
                    Not started &middot; {lesson.tasks.length} tasks
                  </p>
                )}
              </div>

              <ArrowRight className="hidden h-4 w-4 shrink-0 text-muted-foreground/40 transition-all group-hover:translate-x-1 group-hover:text-primary sm:block" />
            </Link>
          );
        })}
      </div>
    </div>
  );
}
