"use client";

import React, { useEffect } from "react";

import { useState, useCallback } from "react";
import { Task } from "@/types/task";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { CheckCircle2, ChevronDown, ChevronUp, Lightbulb, RotateCcw, Send } from "lucide-react";
import { DifficultyLevels } from "@/types/difficultyLevels";
import { GatewayFactory } from "@/config/GatewayFactory";
import { TaskProgress } from "@/types/taskProgress";

interface TaskItemProps {
  task: Task;
  lessonId: string;
  isExpanded: boolean;
  onToggle: () => void;
  onComplete?: (taskId: string, answer: string, isCorrect: boolean) => void;
  isAuthenticated: boolean;
  progress?: TaskProgress;
}

export function TaskItem({ task, lessonId, isExpanded, onToggle, onComplete, isAuthenticated, progress }: TaskItemProps) {
  const [userInput, setUserInput] = useState("");
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string; } | null>(null);
  const [showHint, setShowHint] = useState(false);
  const [showAnswer, setShowAnswer] = useState(false);
  const [localCompleted, setLocalCompleted] = useState(false);

  const isCompleted = (progress?.status === "COMPLETED") || localCompleted;
  const savedUserInput = progress?.lastInput ?? "";
  const totalAttempts = progress?.attempts || 0;
  
  const effectiveStatus = localCompleted || progress?.status === "COMPLETED" ? "COMPLETED" : progress?.status === "IN_PROGRESS" ? "IN_PROGRESS" : "NOT_STARTED";
  const statusVariant = effectiveStatus === "COMPLETED" ? "completed" : effectiveStatus === "IN_PROGRESS" ? "inProgress" : "notStarted";
  const statusLabel = effectiveStatus === "COMPLETED" ? "Completed" : effectiveStatus === "IN_PROGRESS" ? "In Progress" : "Not Started";

  useEffect(() => {
    if (!localCompleted && isCompleted && savedUserInput) {
      setUserInput(savedUserInput);
      setLocalCompleted(true);
      setFeedback({
        type: "success",
        message: "Correct! Well Done!"
      });
    }
  }, [isCompleted, savedUserInput, localCompleted]);

  const handleSubmit = useCallback(async (e: React.SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!userInput.trim()) { return; }

    const isCorrect = userInput.trim().toLowerCase() === task.expectedCommand.toLowerCase();
    
    setFeedback({ type: isCorrect ? "success" : "error", message: isCorrect ? "Correct!" : "Not quite right. Try again!"});

    if(isCorrect) { setLocalCompleted(true); }
    if(isAuthenticated) { await GatewayFactory.instance.taskProgressGateway.recordTaskAttempt( lessonId, task.taskId, userInput ); }
    if(onComplete) { onComplete(task.taskId, userInput, isCorrect); }
  }, [userInput, task.expectedCommand, task.taskId, isCompleted, onComplete, task.expectedCommand]);

  const handleReset = useCallback(() => {
    setUserInput("");
    setFeedback(null);
    setShowHint(false);
    setLocalCompleted(false);
  }, []);

  return (
    <div title={`${isExpanded ? "Collapse task" : "Expand task"}`}
    className={`rounded-xl border transition-all ${isCompleted ? "border-primary/20 bg-primary/5" : "border-border bg-card"} p-3 sm:p-4`}>
      <button type="button" onClick={onToggle} className="flex w-full items-center gap-3 p-3 text-left sm:p-4">
        <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-secondary">
          {isCompleted ? (<CheckCircle2 className="h-4 w-4 text-primary" />) : (<span className="text-xs font-medium text-secondary-foreground">{task.taskOrder}</span>)}
        </div>

        <div className="min-w-0 flex-1">
          <div className="flex flex-wrap items-center gap-2">
            <h3 className={`text-base sm:text-lg font-medium sm:text-base ${isCompleted ? "text-primary" : "text-card-foreground"}`}>
              {task.title}
            </h3>
            <Badge variant={task.difficulty.toLowerCase() as DifficultyLevels}>
              {task.difficulty.charAt(0).toUpperCase() + task.difficulty.slice(1).toLowerCase()}
            </Badge>
          </div>
          <span className="text-md text-muted-foreground flex items-center gap-2">
            {totalAttempts > 0 && (
              <>
                {totalAttempts} attempt{totalAttempts !== 1 ? "s" : ""}
                <span>&middot;</span>
              </>
            )}
            <Badge variant={statusVariant}>{statusLabel}</Badge>
          </span>

        </div>

        {isExpanded ? (<ChevronUp className="h-4 w-4 shrink-0 text-muted-foreground" />) : (<ChevronDown className="h-4 w-4 shrink-0 text-muted-foreground" />)}
      </button>

      {isExpanded && (
        <div className="border-t border-border px-3 pb-3 pt-3 sm:px-4 sm:pb-4">
          <p className="mb-4 text-md leading-relaxed text-muted-foreground">
            {task.content}
          </p>

          {showHint ? (
            <div className="mb-4 flex items-start gap-2 rounded-lg bg-secondary p-3">
              <button type="button" title="Hide hint" onClick={() => setShowHint(false)}>
                <Lightbulb className="h-4 w-4 shrink-0 text-warning" />
              </button>
              <p className="text-sm text-secondary-foreground">{task.hint}</p>
            </div>
          ) : (
            <button type="button" title="View hint" onClick={() => setShowHint(true)}
            className="mb-4 flex items-center gap-1.5 text-md text-muted-foreground transition-colors hover:text-foreground">
              <Lightbulb className="h-3.5 w-3.5" />
              Show hint
            </button>
          )}

          <form onSubmit={handleSubmit} className="flex flex-col gap-2 sm:flex-row">
            <div className="relative flex-1">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 font-mono text-sm text-primary">
                $
              </span>
              <Input value={userInput} onChange={(e) => { setUserInput(e.target.value); if(feedback) setFeedback(null); }}
              placeholder="Type your git command..." className="bg-secondary pl-7 font-mono text-sm text-foreground placeholder:text-muted-foreground placeholder:text-lg"/>
            </div>
            <div className="flex gap-2">
              <Button type="submit" size="default" disabled={!userInput.trim()} title="Submit your answer"
              className={`flex-1 sm:flex-none transition-all duration-200 ${userInput.trim() || !isCompleted ? "hover:-translate-y-0.5 hover:shadow-md hover:shadow-primary/20" : ""}`}>
                <Send className="h-4 w-4" />
                <span className="sm:hidden">Submit</span>
                <span className="sr-only sm:not-sr-only">
                  <span className="sr-only">Submit answer</span>
                </span>
              </Button>
              <Button type="button" variant="outline" size="icon" onClick={handleReset} title="Reset your answer" className="shrink-0 bg-transparent group">
                <RotateCcw className="h-4 w-4 transition-transform duration-300 group-hover:-rotate-180" />
                <span className="sr-only">Reset</span>
              </Button>
            </div>
          </form>

          {feedback && (
            <div className={`mt-3 rounded-xl px-3 py-2 text-xl ${feedback.type === "success" ? "text-green-700 bg-green-200" : "text-red-700 bg-destructive/10 text-destructive"}`}>
              {feedback.message}
            </div>
          )}

          {totalAttempts >= 3 &&
            <>
              {showAnswer ? (
                <div className="mb-4 flex items-start gap-2 rounded-lg bg-secondary p-3 mt-2">
                  <button type="button" title="Hide answer" onClick={() => setShowAnswer(false)}>
                  </button>
                  <p className="text-sm text-secondary-foreground">Expected answer: {task.expectedCommand}</p>
                </div>
              ) : (
                <button type="button" title="View answer" onClick={() => setShowAnswer(true)}
                className="mb-4 flex items-center gap-1.5 text-md text-muted-foreground transition-colors hover:text-foreground mt-2">
                  Show answer
                </button>
              )}
            </>
          }
        </div>
      )}
    </div>
  );
}