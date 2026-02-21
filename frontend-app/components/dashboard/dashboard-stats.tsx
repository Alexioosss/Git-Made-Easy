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
      label: "Lessons Completed",
      value: `${completedLessons}/${totalLessons}`,
      percent: lessonPercent,
      icon: BookOpen,
      color: "text-primary",
      bgColor: "bg-primary/10",
    },
    {
      label: "Tasks Completed",
      value: `${totalTasksCompleted}/${totalTasks}`,
      percent: taskPercent,
      icon: CheckCircle2,
      color: "text-primary",
      bgColor: "bg-primary/10",
    },
    {
      label: "Total Attempts",
      value: totalAttempts,
      percent: null,
      icon: Target,
      color: "text-warning",
      bgColor: "bg-warning/10",
    }
  ];

  return (
    <div className="grid grid-cols-2 gap-3 sm:gap-3 lg:grid-cols-3">
      {stats.map((stat) => (
        <div key={stat.label} className="rounded-xl border border-border bg-card p-4 sm:p-5">
          <div className="flex items-center gap-2 sm:gap-3">
            <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg sm:h-10 sm:w-10 ${stat.bgColor}`}>
              <stat.icon className={`h-4 w-4 sm:h-5 sm:w-5 ${stat.color}`} />
            </div>
            <div className="min-w-0 flex-1">
              <p className="truncate text-xl font-bold text-card-foreground sm:text-2xl">
                {stat.value}
              </p>
              <p className="truncate text-xs text-muted-foreground">
                {stat.label}
              </p>
            </div>
          </div>
          {stat.percent !== null && (
            <div className="mt-3 flex items-center gap-2">
              <Progress value={stat.percent} className="h-1.5 flex-1" />
              <span className="shrink-0 text-xs font-medium text-muted-foreground">
                {stat.percent}%
              </span>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}