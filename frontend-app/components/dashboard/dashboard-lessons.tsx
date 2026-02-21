import Link from "next/link";
import { Progress } from "@/components/ui/progress";
import { ArrowRight, CheckCircle2 } from "lucide-react";
import { DashboardData } from "@/types/dashboard";

interface DashboardLessonsProps {
  lessons: DashboardData["lessons"];
}

export function DashboardLessons({ lessons }: DashboardLessonsProps) {
  const hasLessons = lessons && lessons.length > 0;

  return (
    <div>
      <h2 className="mb-4 text-lg font-semibold text-foreground">
        Your Lessons
      </h2>

      {!hasLessons && (
        <div className="rounded-xl border border-border bg-card p-6 text-center sm:p-10">
          <p className="text-base text-muted-foreground sm:text-lg">
            You haven't started any lessons yet.
          </p>

          <Link href="/lessons" title="View lessons catalog" className="mt-4 inline-flex items-center gap-1 text-sm text-primary transition-transform hover:scale-110 sm:text-base" >
            Browse lessons
            <ArrowRight className="h-4 w-4 sm:h-5 sm:w-5" />
          </Link>
        </div>
      )}

      <div className="flex flex-col gap-3">
        {lessons.map((lesson) => {
          const progressPercentage = lesson.totalTasksCount > 0 ? (lesson.completedTasksCount / lesson.totalTasksCount * 100) : 0;
          const isComplete = progressPercentage === 100;
          const hasStarted = lesson.completedTasksCount > 0;

          return (
            <Link key={lesson.lessonId} href={`/lessons/${lesson.lessonId}`}
            className="group flex items-center gap-3 rounded-xl border border-border bg-card p-3.5 transition-all hover:border-primary/30 sm:gap-4 sm:p-4">
              <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg sm:h-10 sm:w-10 ${isComplete ? "bg-primary/20" : hasStarted ? "bg-secondary" : "bg-secondary"}`}>
                {isComplete ? ( <CheckCircle2 className="h-4 w-4 text-primary sm:h-5 sm:w-5" /> ) : (
                  <span className="text-xs font-semibold text-secondary-foreground sm:text-sm">
                    {String(lesson.lessonOrder).padStart(2, "0")}
                  </span>
                )}
              </div>

              <div className="min-w-0 flex-1">
                <div className="flex items-center justify-between gap-2">
                  <h3 className="truncate text-sm font-medium text-card-foreground group-hover:text-primary transition-colors">
                    {lesson.title}
                  </h3>
                  {hasStarted && (
                    <span className="shrink-0 text-sm font-semibold text-foreground">
                      {progressPercentage}%
                    </span>
                  )}
                </div>
                {progressPercentage ? (
                  <div className="mt-2 flex items-center gap-2 sm:gap-3">
                    <Progress value={progressPercentage} className="h-1.5 flex-1"/>
                    <span className="shrink-0 text-xs text-muted-foreground">
                      {lesson.completedTasksCount}/{lesson.totalTasksCount}
                    </span>
                  </div>
                ) : (
                  <p className="mt-1 text-xs text-muted-foreground">
                    Not started &middot; {lesson.totalTasksCount - lesson.completedTasksCount} tasks
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