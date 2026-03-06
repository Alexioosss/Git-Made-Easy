"use client"

import { LessonCard } from "@/components/lessons/lesson-card";
import LoadingSpinner from "@/components/LoadingSpinner";
import { GatewayFactory } from "@/config/GatewayFactory";
import { getCurrentUser, hasToken } from "@/lib/auth";
import progressManager from "@/lib/progressManager";
import { safeCallWrapper } from "@/lib/safeCallWrapper";
import { Lesson } from "@/types/lesson";
import { LessonProgress } from "@/types/taskProgress";
import { useEffect, useState } from "react";

export default function LessonPage() {
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [progressMap, setProgressMap] = useState<Record<string, LessonProgress>>({});
    const [loading, setLoading] = useState(true);
    const lessonGateway = GatewayFactory.instance.lessonGateway;
    const lessonProgressGateway = GatewayFactory.instance.lessonProgressGateway;

    useEffect(() => {
        const fetchData = async () => {
            // Fetch all lessons for display
            const response = await safeCallWrapper(() => lessonGateway.getAllLessons());
            if(!response.ok || !response.data) { setLessons([]); setLoading(false); return; }

            const lessons = response.data;

            const lessonsWithTasks = await Promise.all(
                lessons.map(async (lesson) => {
                    const tasksResult = await safeCallWrapper(() => lessonGateway.getTasksForLesson(lesson.lessonId));
                    return {
                        ...lesson,
                        tasks: Array.isArray(tasksResult.data) ? tasksResult.data : []
                    }
                })
            );
            setLessons(lessonsWithTasks);

            const userResult = await safeCallWrapper(() => getCurrentUser());
            if(!userResult.ok || !userResult.data) { setLoading(false); return; }
            
            // Fetch progress for all tasks for this lesson
            const progressResult = await safeCallWrapper(() => lessonProgressGateway.getAllLessonsProgress());
            if(progressResult.ok && progressResult.data) {
                const progressList: LessonProgress[] = progressResult.data;
                const progressMap = Object.fromEntries(progressList.map(progress => [progress.lessonId, progress]));
                setProgressMap(progressMap);
            } else {
                const progressList = await progressManager.getProgress();
                setProgressMap(progressManager.convertLocalToLessonProgress(progressList, lessons));
            }
            setLoading(false);
        };
        fetchData();
    }, []);

    if(loading) {
        return ( <LoadingSpinner message="Loading lessons, please wait..." /> );
    }

    if(!loading && lessons.length === 0) {
        return (
            <div className="min-h-[calc(100dvh-3.5rem)] sm:min-h-[calc(100dvh-4rem)] flex items-center justify-center px-6">
                <p className="text-center text-foreground text-2xl">
                    Could not load lessons.
                    <span className="block sm:inline"> Please try again later.</span>
                </p>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-background py-12">
            <div className="mx-auto max-w-6xl px-4 py-8 sm:py-12">
                <div className="mb-8 sm:mb-10">
                    <h1 className="text-balance text-2xl font-bold text-foreground sm:text-3xl md:text-4xl">
                        All Lessons
                    </h1>
                    <p className="mt-2 max-w-2xl text-sm text-muted-foreground sm:mt-3 sm:text-base">
                        Work through lessons at your own pace. Each lesson contains tasks of
                        varying difficulty to help you master Git concepts.
                    </p>
                </div>
        
                <div className="grid gap-4 sm:gap-6">
                    {lessons.map((lesson) => (
                    <LessonCard key={lesson.lessonId} lesson={lesson} progress={progressMap[lesson.lessonId]} />
                    ))}
                </div>
            </div>
        </div>
    );
}