import { Progress } from "@/components/ui/progress";
import { BookOpen, CheckCircle2, Target, Flame } from "lucide-react";
import { DashboardData } from "@/types/dashboard";

export function DashboardStats({ data } : { data: DashboardData }) {
  const lessons = data.lessons ?? [];
  const tasksProgress = data.tasksProgress ?? [];

  const totalLessons = lessons.length;
  const completedLessons = lessons.filter((lesson) => lesson.totalTasksCount > 0 && lesson.completedTasksCount === lesson.totalTasksCount).length;
  const lessonPercent = totalLessons > 0 ? Math.round((completedLessons / totalLessons) * 100) : 0;
  const totalTasks = lessons.reduce((sum, lesson) => sum + lesson.totalTasksCount, 0);
  const totalTasksCompleted = lessons.reduce((sum, lesson) => sum + lesson.completedTasksCount, 0);
  const taskPercent = totalTasks > 0 ? Math.round((totalTasksCompleted / totalTasks) * 100) : 0;
  const totalAttempts = tasksProgress.reduce((sum, task) => sum + task.attempts, 0);

  const stats = [
    {
      title: "Lessons Completed",
      value: `${completedLessons}/${totalLessons}`,
      percentage: lessonPercent,
      icon: BookOpen,
      colour: "text-primary",
      backgroundColour: "bg-primary/10",
    },
    {
      title: "Tasks Completed",
      value: `${totalTasksCompleted}/${totalTasks}`,
      percentage: taskPercent,
      icon: CheckCircle2,
      colour: "text-primary",
      backgroundColour: "bg-primary/10",
    },
    {
      title: "Total Attempts",
      value: totalAttempts,
      percentage: null,
      icon: Target,
      colour: "text-warning",
      backgroundColour: "bg-warning/10",
    }
  ];

  return (
    <div className="grid grid-cols-2 gap-3 sm:gap-3 lg:grid-cols-3">
      {stats.map((stat) => (
        <div key={stat.title} className="rounded-xl border border-border bg-card p-4 sm:p-5">
          <div className="flex items-center gap-2 sm:gap-3">
            <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg sm:h-10 sm:w-10 ${stat.backgroundColour}`}>
              <stat.icon className={`h-4 w-4 sm:h-5 sm:w-5 ${stat.colour}`} />
            </div>
            <div className="min-w-0 flex-1">
              <p className="truncate text-xl font-bold text-card-foreground sm:text-2xl">
                {stat.value}
              </p>
              <p className="truncate text-md text-muted-foreground">
                {stat.title}
              </p>
            </div>
          </div>
          {stat.percentage !== null && (
            <div className="mt-3 flex items-center gap-2">
              <Progress value={stat.percentage} className="h-1.5 flex-1" />
              <span className="shrink-0 text-xs font-medium text-muted-foreground">
                {stat.percentage}%
              </span>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}