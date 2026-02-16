"use client";

import { use } from "react";
import { notFound } from "next/navigation";
import { SiteHeader } from "@/components/site-header";
import { TaskList } from "@/components/lessons/task-list";
import { LessonHeader } from "@/components/lessons/lesson-header";
import { mockLessons, mockLessonProgress } from "@/lib/mock-data";
import { useAuth } from "@/lib/auth-context";

export default function LessonDetailPage({params}: {params: Promise<{ lessonId: string }>}) {
  const { lessonId } = use(params);
  const { isAuthenticated } = useAuth();
  const lesson = mockLessons.find((l) => l.lessonId === lessonId);

  if (!lesson) { notFound(); }

  const progress = isAuthenticated ? mockLessonProgress[lesson.lessonId] : undefined;

  // Find the next lesson by orderIndex
  const sortedLessons = [...mockLessons].sort((a, b) => a.orderIndex - b.orderIndex);
  const currentIndex = sortedLessons.findIndex((l) => l.lessonId === lesson.lessonId);
  const nextLesson = currentIndex >= 0 && currentIndex < sortedLessons.length - 1 ? sortedLessons[currentIndex + 1] : null;

  return (
    <div className="min-h-screen bg-background">
      <SiteHeader />
      <main className="mx-auto max-w-4xl px-4 py-8 sm:py-12">
        <LessonHeader lesson={lesson} progress={progress} />
        <TaskList lesson={lesson} nextLesson={nextLesson} />
      </main>
    </div>
  );
}