"use client";

import { useState, useCallback, useMemo } from "react";
import Link from "next/link";
import type { Lesson } from "@/lib/types";
import { useAuth } from "@/lib/auth-context";
import { mockTaskProgress } from "@/lib/mock-data";
import { TaskItem } from "@/components/lessons/task-item";
import { Progress } from "@/components/ui/progress";
import { Button } from "@/components/ui/button";
import { ArrowRight, PartyPopper } from "lucide-react";

interface TaskListProps {
  lesson: Lesson;
  nextLesson: Lesson | null;
}

export function TaskList({ lesson, nextLesson }: TaskListProps) {
  const { isAuthenticated } = useAuth();
  const [expandedTaskId, setExpandedTaskId] = useState<string | null>(null);

  // Track which tasks were completed during this session
  const [sessionCompletedIds, setSessionCompletedIds] = useState<Set<string>>(
    new Set()
  );

  // Build set of already-completed task ids (from saved progress)
  const savedCompletedIds = useMemo(() => {
    if (!isAuthenticated) return new Set<string>();
    const ids = new Set<string>();
    for (const task of lesson.tasks) {
      const progress = mockTaskProgress[task.taskId];
      if (progress?.completed) {
        ids.add(task.taskId);
      }
    }
    return ids;
  }, [isAuthenticated, lesson.tasks]);

  const allCompleted = lesson.tasks.every(
    (t) => savedCompletedIds.has(t.taskId) || sessionCompletedIds.has(t.taskId)
  );

  const completedCount =
    new Set([...savedCompletedIds, ...sessionCompletedIds]).size;

  const completionPercent =
    lesson.tasks.length > 0
      ? Math.round((completedCount / lesson.tasks.length) * 100)
      : 0;

  const handleTaskComplete = useCallback((taskId: string) => {
    setSessionCompletedIds((prev) => new Set(prev).add(taskId));
  }, []);

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-semibold text-foreground">Tasks</h2>
          <div className="flex items-center gap-2">
            <span className="text-sm font-medium text-foreground">
              {completionPercent}%
            </span>
            <span className="text-sm text-muted-foreground">
              ({completedCount}/{lesson.tasks.length} completed)
            </span>
          </div>
        </div>
        <Progress value={completionPercent} className="h-1.5" />
      </div>

      {lesson.tasks.map((task) => (
        <TaskItem
          key={task.taskId}
          task={task}
          lessonId={lesson.lessonId}
          isExpanded={expandedTaskId === task.taskId}
          onToggle={() =>
            setExpandedTaskId(
              expandedTaskId === task.taskId ? null : task.taskId
            )
          }
          onComplete={handleTaskComplete}
        />
      ))}

      {/* Next lesson banner */}
      {allCompleted && (
        <div className="mt-4 flex flex-col items-center gap-4 rounded-xl border border-primary/20 bg-primary/5 p-6 text-center">
          <div className="flex items-center gap-2 text-primary">
            <PartyPopper className="h-5 w-5" />
            <span className="font-semibold">All tasks completed!</span>
          </div>
          {nextLesson ? (
            <>
              <p className="text-sm text-muted-foreground">
                Ready to move on? Continue to{" "}
                <span className="text-foreground">{nextLesson.title}</span>.
              </p>
              <Button asChild>
                <Link href={`/lessons/${nextLesson.lessonId}`}>
                  Next Lesson
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Link>
              </Button>
            </>
          ) : (
            <p className="text-sm text-muted-foreground">
              You have finished all available lessons. Great work!
            </p>
          )}
        </div>
      )}
    </div>
  );
}
