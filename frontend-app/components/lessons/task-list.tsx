"use client";

import ProgressManager from "@/lib/progressManager";
import { useState, useCallback, useMemo, useEffect } from "react";
import Link from "next/link";
import { Lesson } from "@/types/lesson";
import { TaskItem } from "@/components/lessons/task-item";
import { Progress } from "@/components/ui/progress";
import { Button } from "@/components/ui/button";
import { ArrowRight, PartyPopper } from "lucide-react";
import { getCurrentUser, hasToken } from "@/lib/auth";
import { GatewayFactory } from "@/config/GatewayFactory";
import { LocalTaskProgress } from "@/infrastructure/persistence/localProgressData";
import progressManager from "@/lib/progressManager";

interface TaskListProps {
  lesson: Lesson;
  nextLesson?: Lesson | null;
}

export function TaskList({ lesson, nextLesson }: TaskListProps) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [expandedTaskIds, setExpandedTaskIds] = useState<Set<string>>(new Set());
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
    if(Object.keys(taskProgressMap).length === 0) return;
    const nextTask = lesson.tasks.find(task => { const p = taskProgressMap[task.taskId]; return p?.status !== "COMPLETED"; });
    if(nextTask) { setExpandedTaskIds(prev => { const next = new Set(prev); next.add(nextTask.taskId); return next; }); }
  }, [lesson.tasks, taskProgressMap]);

  useEffect(() => {
    async function loadProgress() {
      let map: Record<string, any> = {};
      if(isAuthenticated) {
        for(const task of lesson.tasks) {
          try {
            const progress = await GatewayFactory.instance.taskProgressGateway.getTaskProgress(lesson.lessonId, task.taskId);
            map[task.taskId] = progress;
          } catch { map[task.taskId] = { status: "NOT_STARTED", attempts: 0 }; }
        }
      } else {
        const raw = await progressManager.getProgress();
        const flat: Record<string, any> = {};
        for(const lessonId in raw) {
          const lesson = raw[lessonId];
          for(const taskId in lesson.completedTasks) {
            flat[taskId] = lesson.completedTasks[taskId];
          }
        }
        for(const task of lesson.tasks) {
          if(!flat[task.taskId]) {
            flat[task.taskId] = { status: "NOT_STARTED", attempts: 0 };
          }
        }
        map = flat;
        for(const task of lesson.tasks) {
          if(!map[task.taskId]) {
            map[task.taskId] = { status: "NOT_STARTED", attempts: 0 };
          }
        }
      }
      setTaskProgressMap(map);
    }
    loadProgress();
  }, [isAuthenticated, lesson.lessonId, lesson.tasks]);

  const toggleTask = (taskId: string) => {
    setExpandedTaskIds(prev => {
      const next = new Set(prev);
      if(next.has(taskId)) next.delete(taskId);
      else next.add(taskId);
      return next;
    });
  };

  const completedCount = lesson.tasks.filter((task) => { const progress = taskProgressMap[task.taskId]; return progress?.status === "COMPLETED"; }).length;
  const allCompleted = completedCount === lesson.tasks.length;
  const completionPercentage = lesson.tasks.length > 0 ? Math.round((completedCount / lesson.tasks.length) * 100) : 0;

  const handleTaskComplete = useCallback(async (taskId: string, answer: string, isCorrect: boolean) => {
    const prev = taskProgressMap[taskId];
    const newAttempts = (prev?.attempts || 0) + 1;
    const newStatus = isCorrect ? "COMPLETED" : prev?.status === "COMPLETED" ? "COMPLETED" : "IN_PROGRESS";
    const taskProgress: LocalTaskProgress = {
      taskId,
      status: newStatus,
      attempts: newAttempts,
      lastInput: answer,
      lastError: "",
      startedAt: prev?.startedAt || new Date().toISOString(),
      completedAt: newStatus === "COMPLETED" ? prev?.completedAt || new Date().toISOString() : prev?.completedAt
    };

    if (!hasToken()) { await ProgressManager.updateLesson(lesson.lessonId, taskProgress); }
    else {} // Backend already recorded the user attempt
    setTaskProgressMap(prevMap => ({ ...prevMap, [taskId]: taskProgress }));

  }, [lesson.lessonId, taskProgressMap]);

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-semibold text-foreground">Tasks</h2>
          <div className="flex items-center gap-2">
            <span className="text-sm font-medium text-foreground">
              {completionPercentage}%
            </span>
            <span className="text-sm text-muted-foreground">
              ({completedCount}/{lesson.tasks.length} completed)
            </span>
          </div>
        </div>
        <Progress value={completionPercentage} className="h-1.5" />
      </div>

      {lesson.tasks.map((task) => (
        <TaskItem key={task.taskId} task={task} lessonId={lesson.lessonId} isExpanded={expandedTaskIds.has(task.taskId)}
        onToggle={() => toggleTask(task.taskId)}
        onComplete={handleTaskComplete} isAuthenticated={isAuthenticated} progress={taskProgressMap[task.taskId]} />
      ))}

      {/* Next lesson banner */}
      {allCompleted && (
        <div className="mt-4 flex flex-col items-center gap-4 rounded-xl border border-green-300 bg-green-300 p-6 text-center">
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
              <p className="text-md text-muted-foreground">
                Ready to move on? Continue to{" "}
                <span className="text-foreground">{nextLesson.title}</span>.
              </p>
              <Button asChild>
                <Link href={`/lessons/${nextLesson.lessonId}`} title="Go to the next lesson">
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