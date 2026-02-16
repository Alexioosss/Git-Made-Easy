"use client"

import { notFound } from "next/navigation";
import { TaskList } from "@/components/lessons/task-list";
import { LessonHeader } from "@/components/lessons/lesson-header";
import { mockLessons, mockLessonProgress } from "@/lib/mock-data";

export default function LessonDetailClient({ lessonId }: { lessonId: string }) {
    const lesson = mockLessons.find((l) => l.lessonId === lessonId);
    const isAuthenticated = true; // Replace with actual authentication logic

    if (!lesson) { notFound(); }

    const progress = isAuthenticated ? mockLessonProgress[lesson.lessonId] : undefined;
    const sortedLessons = [...mockLessons].sort((a, b) => a.orderIndex - b.orderIndex);
    const currentIndex = sortedLessons.findIndex((l) => l.lessonId === lesson.lessonId);
    const nextLesson = currentIndex >= 0 && currentIndex < sortedLessons.length - 1 ? sortedLessons[currentIndex + 1] : null;

    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-4xl px-4 py-8 sm:py-12">
                <LessonHeader lesson={lesson} progress={progress} />
                <TaskList lesson={lesson} nextLesson={nextLesson} />
            </div>
        </div>
    );
}