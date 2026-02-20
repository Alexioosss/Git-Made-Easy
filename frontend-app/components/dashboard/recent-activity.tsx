import { CheckCircle2, XCircle } from "lucide-react";

export async function RecentActivity() {

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
                    {activity.date}
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