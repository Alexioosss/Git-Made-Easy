"use client";

import React from "react";

import { useState, useCallback } from "react";
import { Task } from "@/types/task";
import { mockTaskProgress } from "@/lib/mock-data";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  CheckCircle2,
  ChevronDown,
  ChevronUp,
  Lightbulb,
  RotateCcw,
  Send,
} from "lucide-react";

interface TaskItemProps {
  task: Task;
  lessonId: string;
  isExpanded: boolean;
  onToggle: () => void;
  onComplete?: (taskId: string) => void;
}

export function TaskItem({ task, lessonId, isExpanded, onToggle, onComplete }: TaskItemProps) {
  const isAuthenticated = false;
  const [answer, setAnswer] = useState("");
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string; } | null>(null);
  const [showHint, setShowHint] = useState(false);
  const [localAttempts, setLocalAttempts] = useState(0);
  const [localCompleted, setLocalCompleted] = useState(false);

  const difficultyVariant: Record<Task["difficulty"], "easy" | "medium" | "hard"> = {
    EASY: "easy",
    MEDIUM: "medium",
    HARD: "hard",
  };

  // Check if user has existing progress (for logged-in users)
  const existingProgress = isAuthenticated
    ? mockTaskProgress[task.taskId]
    : undefined;
  const isCompleted = existingProgress?.completed || localCompleted;
  const totalAttempts = (existingProgress?.attempts || 0) + localAttempts;

  const handleSubmit = useCallback(
    (e: React.FormEvent) => {
      e.preventDefault();
      if (!answer.trim()) return;

      setLocalAttempts((prev) => prev + 1);

      const isCorrect =
        answer.trim().toLowerCase() === task.expectedAnswer.toLowerCase();

      if (isCorrect) {
        const wasAlreadyCompleted = isCompleted;
        setLocalCompleted(true);
        setFeedback({
          type: "success",
          message: wasAlreadyCompleted
            ? "Correct again! Attempt recorded."
            : "Correct! Well done.",
        });
        if (!wasAlreadyCompleted && onComplete) {
          onComplete(task.taskId);
        }
      } else {
        setFeedback({
          type: "error",
          message: "Not quite right. Try again!",
        });
      }
    },
    [answer, task.expectedAnswer, task.taskId, isCompleted, onComplete]
  );

  const handleReset = useCallback(() => {
    setAnswer("");
    setFeedback(null);
    setShowHint(false);
  }, []);

  return (
    <div
      className={`rounded-xl border transition-all ${
        isCompleted
          ? "border-primary/20 bg-primary/5"
          : "border-border bg-card"
      }`}
    >
      {/* Task header - always visible */}
      <button
        type="button"
        onClick={onToggle}
        className="flex w-full items-center gap-3 p-4 text-left sm:gap-4 sm:p-5"
      >
        <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-secondary">
          {isCompleted ? (
            <CheckCircle2 className="h-4 w-4 text-primary" />
          ) : (
            <span className="text-xs font-medium text-secondary-foreground">
              {task.orderIndex}
            </span>
          )}
        </div>

        <div className="min-w-0 flex-1">
          <div className="flex flex-wrap items-center gap-2">
            <h3 className={`text-sm font-medium sm:text-base ${isCompleted ? "text-primary" : "text-card-foreground"}`}>
              {task.title}
            </h3>
            <Badge variant={difficultyVariant[task.difficulty]}>
              {task.difficulty.toLowerCase()}
            </Badge>
          </div>
          {totalAttempts > 0 && (
            <span className="text-xs text-muted-foreground">
              {totalAttempts} attempt{totalAttempts !== 1 ? "s" : ""}
            </span>
          )}
        </div>

        {isExpanded ? (
          <ChevronUp className="h-4 w-4 shrink-0 text-muted-foreground" />
        ) : (
          <ChevronDown className="h-4 w-4 shrink-0 text-muted-foreground" />
        )}
      </button>

      {/* Expanded content */}
      {isExpanded && (
        <div className="border-t border-border px-4 pb-4 pt-4 sm:px-5 sm:pb-5">
          <p className="mb-4 text-sm leading-relaxed text-muted-foreground">
            {task.description}
          </p>

          {/* Hint toggle */}
          {showHint ? (
            <div className="mb-4 flex items-start gap-2 rounded-lg bg-secondary p-3">
              <Lightbulb className="mt-0.5 h-4 w-4 shrink-0 text-warning" />
              <p className="text-sm text-secondary-foreground">{task.hint}</p>
            </div>
          ) : (
            <button
              type="button"
              onClick={() => setShowHint(true)}
              className="mb-4 flex items-center gap-1.5 text-xs text-muted-foreground transition-colors hover:text-foreground">
              <Lightbulb className="h-3.5 w-3.5" />
              Show hint
            </button>
          )}

          {/* Answer form */}
          <form
            onSubmit={handleSubmit}
            className="flex flex-col gap-2 sm:flex-row"
          >
            <div className="relative flex-1">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 font-mono text-sm text-primary">
                $
              </span>
              <Input
                value={answer}
                onChange={(e) => {
                  setAnswer(e.target.value);
                  if (feedback) setFeedback(null);
                }}
                placeholder="Type your git command..."
                className="bg-secondary pl-7 font-mono text-sm text-foreground placeholder:text-muted-foreground"
              />
            </div>
            <div className="flex gap-2">
              <Button
                type="submit"
                size="default"
                disabled={!answer.trim()}
                className="flex-1 sm:flex-none">
                <Send className="mr-2 h-4 w-4 sm:mr-0" />
                <span className="sm:hidden">Submit</span>
                <span className="sr-only sm:not-sr-only">
                  <span className="sr-only">Submit answer</span>
                </span>
              </Button>
              <Button
                type="button"
                variant="outline"
                size="icon"
                onClick={handleReset}
                className="shrink-0 bg-transparent">
                <RotateCcw className="h-4 w-4" />
                <span className="sr-only">Reset</span>
              </Button>
            </div>
          </form>

          {/* Feedback */}
          {feedback && (
            <div className={`mt-3 rounded-lg px-3 py-2 text-sm ${feedback.type === "success" ? "bg-primary/10 text-primary" : "bg-destructive/10 text-destructive"}`}>
              {feedback.message}
            </div>
          )}

          {/* Save progress note for non-logged-in users */}
          {!isAuthenticated && isCompleted && (
            <p className="mt-3 text-xs text-muted-foreground">
              Sign in to save your progress across sessions.
            </p>
          )}
        </div>
      )}
    </div>
  );
}