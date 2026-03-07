"use client";

import ProgressManager from "@/context/progressManager";
import { useState, useCallback, useMemo, useEffect, useRef } from "react";
import Link from "next/link";
import { Lesson } from "@/types/lesson";
import { TaskItem } from "@/components/lessons/task-item";
import { Progress } from "@/components/ui/progress";
import { Button } from "@/components/ui/button";
import { ArrowRight, PartyPopper } from "lucide-react";
import { getCurrentUser, hasToken } from "@/lib/auth";
import { GatewayFactory } from "@/config/GatewayFactory";
import { LocalTaskProgress } from "@/types/localProgressData";
import progressManager from "@/context/progressManager";
import { ProgressStatus } from "@/types/progressStatus";

interface TaskListProps {
  lesson: Lesson;
  nextLesson?: Lesson | null;
}

export function TaskList({ lesson, nextLesson }: TaskListProps) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [expandedTaskIds, setExpandedTaskIds] = useState<Set<string>>(new Set());
  const [taskProgressMap, setTaskProgressMap] = useState<Record<string, any>>({});
  const inputReferences = useRef<Record<string, HTMLInputElement | null>>({});
  const completionReference = useRef<HTMLDivElement | null>(null);
  
  const completedCount = lesson.tasks.filter((task) => { const progress = taskProgressMap[task.taskId]; return progress?.status === ProgressStatus.COMPLETED; }).length;
  const allCompleted = completedCount === lesson.tasks.length;
  const completionPercentage = lesson.tasks.length > 0 ? Math.round((completedCount / lesson.tasks.length) * 100) : 0;

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
    const nextTask = lesson.tasks.find(task => { const p = taskProgressMap[task.taskId]; return p?.status !== ProgressStatus.COMPLETED; });
    if(nextTask) { setExpandedTaskIds(prev => { const next = new Set(prev); next.add(nextTask.taskId); return next; }); }
  }, [lesson.tasks, taskProgressMap]);

  useEffect(() => {
    async function loadProgress() {
      let map: Record<string, any> = {};
      if(isAuthenticated) { // Logged in - progress comes from backend
        for(const task of lesson.tasks) {
          try {
            const progress = await GatewayFactory.instance.taskProgressGateway.getTaskProgress(lesson.lessonId, task.taskId);
            map[task.taskId] = progress;
          } catch { map[task.taskId] = { status: ProgressStatus.NOT_STARTED, attempts: 0 }; }
        }
        setTaskProgressMap(map);
        return;
      }

      // Logged out - progress loaded from local storage
      const raw = await progressManager.getProgress();
      const lessonProgress = raw[lesson.lessonId]?.tasks ?? {};
      map = {};
      for(const task of lesson.tasks) {
        map[task.taskId] = lessonProgress[task.taskId] ?? { status: ProgressStatus.NOT_STARTED, attempts: 0 };
      }
      setTaskProgressMap(map);
    }
    loadProgress();
  }, [isAuthenticated, lesson.lessonId, lesson.tasks]);

  useEffect(() => {
    if(!allCompleted) { return; }
    setTimeout(() => {
      const el = completionReference.current;
      if(!el) { return; }
      el.scrollIntoView({ behavior: "smooth", block: "start" });
      const button = el.querySelector("a, button") as HTMLElement | null;
      button?.focus();
    }, 400);
  }, [allCompleted]);

  const toggleTask = (taskId: string) => {
    setExpandedTaskIds(prev => {
      const next = new Set(prev);
      if(next.has(taskId)) next.delete(taskId);
      else next.add(taskId);
      return next;
    });
  };

  const handleTaskComplete = useCallback(async (taskId: string, answer: string, isCorrect: boolean) => {
    const prev = taskProgressMap[taskId];
    const newAttempts = (prev?.attempts || 0) + 1;
    const newStatus = isCorrect ? ProgressStatus.COMPLETED : prev?.status === ProgressStatus.COMPLETED ? ProgressStatus.COMPLETED : ProgressStatus.IN_PROGRESS;
    const taskProgress: LocalTaskProgress = {
      taskId,
      status: newStatus,
      attempts: newAttempts,
      lastInput: answer,
      lastError: "",
      startedAt: prev?.startedAt || new Date().toISOString(),
      completedAt: newStatus === ProgressStatus.COMPLETED ? prev?.completedAt || new Date().toISOString() : prev?.completedAt
    };

    if (!hasToken()) { await ProgressManager.updateLesson(lesson.lessonId, taskProgress); }
    else {} // Backend already recorded the user attempt
    setTaskProgressMap(prevMap => ({ ...prevMap, [taskId]: taskProgress }));
    if(isCorrect) {
      const tasks = lesson.tasks;
      const currentIndex = tasks.findIndex(t => t.taskId === taskId);
      const nextTask = tasks[currentIndex + 1];
      if(nextTask) {
        setExpandedTaskIds(previous => {
          const next = new Set(previous);
          next.add(nextTask.taskId);
          return next;
        });
        setTimeout(() => {
          const el = document.getElementById(`task-${nextTask.taskId}`);
          el?.scrollIntoView({ behavior: "smooth", block: "start" });
          inputReferences.current[nextTask.taskId]?.focus();
        }, 600);
      }
    }

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
        <TaskItem key={task.taskId} task={task} lessonId={lesson.lessonId} isExpanded={expandedTaskIds.has(task.taskId)} onToggle={() => toggleTask(task.taskId)}
        onComplete={handleTaskComplete} isAuthenticated={isAuthenticated} progress={taskProgressMap[task.taskId]} currentInputReference={(el) => inputReferences.current[task.taskId] = el} />
      ))}

      {/* Next lesson banner */}
      {allCompleted && (
        <div ref={completionReference} className="mt-4 flex flex-col items-center gap-4 rounded-xl border border-green-500/40 bg-green-500/15 p-6 text-center shadow-sm">
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