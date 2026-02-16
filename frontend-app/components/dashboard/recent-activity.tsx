import { mockTaskProgress, mockLessons } from "@/lib/mock-data";
import { CheckCircle2, XCircle } from "lucide-react";

export function RecentActivity() {
  // Build activity list from task progress
  const activities = Object.values(mockTaskProgress)
    .sort(
      (a, b) =>
        new Date(b.lastAttemptDate).getTime() -
        new Date(a.lastAttemptDate).getTime()
    )
    .map((progress) => {
      const lesson = mockLessons.find(
        (l) => l.lessonId === progress.lessonId
      );
      const task = lesson?.tasks.find((t) => t.taskId === progress.taskId);

      return {
        id: progress.taskId,
        taskTitle: task?.title || "Unknown task",
        lessonTitle: lesson?.title || "Unknown lesson",
        completed: progress.completed,
        attempts: progress.attempts,
        date: progress.lastAttemptDate,
      };
    });

  function formatRelativeTime(dateString: string) {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffHours / 24);

    if (diffDays > 0) return `${diffDays}d ago`;
    if (diffHours > 0) return `${diffHours}h ago`;
    return "Just now";
  }

  return (
    <div>
      <h2 className="mb-4 text-lg font-semibold text-foreground">
        Recent Activity
      </h2>
      <div className="rounded-xl border border-border bg-card">
        {activities.length === 0 ? (
          <div className="p-6 text-center text-sm text-muted-foreground">
            No activity yet. Start a lesson to see your progress here.
          </div>
        ) : (
          <div className="divide-y divide-border">
            {activities.map((activity) => (
              <div key={activity.id} className="flex items-start gap-3 p-4">
                {activity.completed ? (
                  <CheckCircle2 className="mt-0.5 h-4 w-4 shrink-0 text-primary" />
                ) : (
                  <XCircle className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
                )}
                <div className="flex-1">
                  <p className="text-sm text-card-foreground">
                    {activity.taskTitle}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {activity.lessonTitle}
                  </p>
                  <p className="mt-1 text-xs text-muted-foreground">
                    {activity.attempts} attempt
                    {activity.attempts !== 1 ? "s" : ""} &middot;{" "}
                    {formatRelativeTime(activity.date)}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
