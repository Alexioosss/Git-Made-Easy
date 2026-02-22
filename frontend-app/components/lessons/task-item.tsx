"use client";

import React from "react";

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
  onComplete?: (taskId: string) => void;
  isAuthenticated: boolean;
  progress?: TaskProgress;
}

export function TaskItem({ task, lessonId, isExpanded, onToggle, onComplete, isAuthenticated, progress }: TaskItemProps) {
  const [answer, setAnswer] = useState("");
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string; } | null>(null);
  const [showHint, setShowHint] = useState(false);
  const [localAttempts, setLocalAttempts] = useState(0);
  const [localCompleted, setLocalCompleted] = useState(false);

  const isCompleted = progress?.status === "COMPLETED" || localCompleted;
  const totalAttempts = (progress?.attempts || 0) + localAttempts;


  const handleSubmit = useCallback(async (e: React.SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!answer.trim()) { return; }

    setLocalAttempts((prev) => prev + 1);
    const isCorrect = answer.trim().toLowerCase() === task.expectedCommand.toLowerCase();
    
    if(isCorrect) {
      const wasAlreadyCompleted = isCompleted;
      setLocalCompleted(true);
      setFeedback({type: "success", message: wasAlreadyCompleted ? "Correct again! Attempt recorded." : "Correct! Well done."});

      if(isAuthenticated) { await GatewayFactory.instance.taskProgressGateway.recordTaskAttempt(lessonId, task.taskId, answer); }
      if(!wasAlreadyCompleted && onComplete) { onComplete(task.taskId); }
      
    } else {
      setFeedback({type: "error", message: "Not quite right. Try again!"});
    }
  }, [answer, task.expectedCommand, task.taskId, isCompleted, onComplete]);

  const handleReset = useCallback(() => {
    setAnswer("");
    setFeedback(null);
    setShowHint(false);
  }, []);

  return (
    <div className={`rounded-xl border transition-all ${isCompleted ? "border-primary/20 bg-primary/5" : "border-border bg-card"} p-3 sm:p-4`}>
      {/* Task header - always visible */}
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
          {totalAttempts > 0 && (<span className="text-xs text-muted-foreground">{totalAttempts} attempt{totalAttempts !== 1 ? "s" : ""}</span>)}
        </div>

        {isExpanded ? (<ChevronUp className="h-4 w-4 shrink-0 text-muted-foreground" />) : (<ChevronDown className="h-4 w-4 shrink-0 text-muted-foreground" />)}
      </button>

      {/* Expanded content */}
      {isExpanded && (
        <div className="border-t border-border px-3 pb-3 pt-3 sm:px-4 sm:pb-4">
          <p className="mb-4 text-md leading-relaxed text-muted-foreground">
            {task.content}
          </p>

          {/* Hint toggle */}
          {showHint ? (
            <div className="mb-4 flex items-start gap-2 rounded-lg bg-secondary p-3">
              <button type="button" title="Hide hint" onClick={() => setShowHint(false)} className="mt-0.5">
                <Lightbulb className="h-4 w-4 shrink-0 text-warning" />
              </button>
              <p className="text-sm text-secondary-foreground">{task.hint}</p>
            </div>
          ) : (
            <button type="button" title="View hint" onClick={() => setShowHint(true)}
            className="mb-4 flex items-center gap-1.5 text-xs text-muted-foreground transition-colors hover:text-foreground">
              <Lightbulb className="h-3.5 w-3.5" />
              Show hint
            </button>
          )}

          {/* Answer form */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-2 sm:flex-row">
            <div className="relative flex-1">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 font-mono text-sm text-primary">
                $
              </span>
              <Input value={answer} onChange={(e) => { setAnswer(e.target.value); if(feedback) setFeedback(null); }}
              placeholder="Type your git command..." className="bg-secondary pl-7 font-mono text-sm text-foreground placeholder:text-muted-foreground"/>
            </div>
            <div className="flex gap-2">
              <Button type="submit" size="default" disabled={!answer.trim()} title="Submit your answer" className="flex-1 sm:flex-none">
                <Send className="mr-2 h-4 w-4 sm:mr-0" />
                <span className="sm:hidden">Submit</span>
                <span className="sr-only sm:not-sr-only">
                  <span className="sr-only">Submit answer</span>
                </span>
              </Button>
              <Button type="button" variant="outline" size="icon" onClick={handleReset} title="Reset your answer" className="shrink-0 bg-transparent">
                <RotateCcw className="h-4 w-4" />
                <span className="sr-only">Reset</span>
              </Button>
            </div>
          </form>

          {/* Feedback */}
          {feedback && (
            <div className={`mt-3 rounded-lg px-3 py-2 text-sm ${feedback.type === "success" ? "text-green-700 bg-green-100" : "text-red-700 bg-destructive/10 text-destructive"}`}>
              {feedback.message}
            </div>
          )}

          {/* Save progress note for non-logged-in users */}
          {!isAuthenticated && isCompleted && (
            <p className="mt-3 text-xs text-muted-foreground">
              P.S. Sign in to save your progress
            </p>
          )}
        </div>
      )}
    </div>
  );
}