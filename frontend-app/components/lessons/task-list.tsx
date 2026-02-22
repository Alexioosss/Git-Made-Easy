"use client";

import { useState, useCallback, useMemo, useEffect } from "react";
import Link from "next/link";
import { Lesson } from "@/types/lesson";
import { TaskItem } from "@/components/lessons/task-item";
import { Progress } from "@/components/ui/progress";
import { Button } from "@/components/ui/button";
import { ArrowRight, PartyPopper } from "lucide-react";
import { getCurrentUser, hasToken } from "@/lib/auth";
import { GatewayFactory } from "@/config/GatewayFactory";

interface TaskListProps {
  lesson: Lesson;
  nextLesson?: Lesson | null;
}

export function TaskList({ lesson, nextLesson }: TaskListProps) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [expandedTaskId, setExpandedTaskId] = useState<string | null>(null);
  const [sessionCompletedIds, setSessionCompletedIds] = useState<Set<string>>(new Set());
  const [taskProgressMap, setTaskProgressMap] = useState<Record<string, any>>({});

  useEffect(() => {
    async function checkAuthentication() {
      if(hasToken()) {
        const user = await getCurrentUser();
        setIsAuthenticated(!!user);
      } else { setIsAuthenticated(false); }
    }
    checkAuthentication();
  }, []);

  useEffect(() => {
    async function loadProgress() {
      if(isAuthenticated) {
        const map: Record<string, any> = {};
        for(const task of lesson.tasks) {
          try {
            const progress = await GatewayFactory.instance.taskProgressGateway.getTaskProgress(lesson.lessonId, task.taskId);
            map[task.taskId] = progress;
          } catch {} // No progress, ignore / continue
        }
        setTaskProgressMap(map);
      }
    }
    loadProgress();
  }, [isAuthenticated, lesson.lessonId, lesson.tasks]);

  const savedCompletedIds = useMemo(() => {
    if(!isAuthenticated) return new Set<string>();
    const ids = new Set<string>();

    for(const task of lesson.tasks) {
      const progress = taskProgressMap[task.taskId];
      if(progress?.status == "COMPLETED") { ids.add(task.taskId); }
    }
    return ids;
  }, [isAuthenticated, lesson.tasks, taskProgressMap]);

  const allCompleted = lesson.tasks.every((t) => savedCompletedIds.has(t.taskId) || sessionCompletedIds.has(t.taskId));
  const completedCount = new Set([...savedCompletedIds, ...sessionCompletedIds]).size;
  const completionPercent = lesson.tasks.length > 0 ? Math.round((completedCount / lesson.tasks.length) * 100) : 0;

  const handleTaskComplete = useCallback(async (taskId: string) => {
    setSessionCompletedIds((prev) => new Set(prev).add(taskId));
  }, [lesson.lessonId]);

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
        <TaskItem key={task.taskId} task={task} lessonId={lesson.lessonId} isExpanded={expandedTaskId === task.taskId}
        onToggle={() => setExpandedTaskId(expandedTaskId === task.taskId ? null : task.taskId)}
        onComplete={handleTaskComplete} isAuthenticated={isAuthenticated} progress={taskProgressMap[task.taskId]} />
      ))}

      {/* Next lesson banner */}
      {allCompleted && (
        <div className="mt-4 flex flex-col items-center gap-4 rounded-xl border border-green-300 bg-green-50 p-6 text-center">
          {lesson.tasks.length === 0 ? (
            <div className="flex items-center gap-2 text-primary">
              <span className="font-semibold">The current lesson has no tasks</span>
            </div>
          ) : (
            <div className="flex items-center gap-2 text-green-700">
              <PartyPopper className="h-5 w-5" />
              <span className="font-semibold">All tasks completed!</span>
            </div>
          )}
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