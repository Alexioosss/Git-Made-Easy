"use client"

import { LessonCard } from "@/components/lessons/lesson-card";
import { GatewayFactory } from "@/config/GatewayFactory";
import { getCurrentUser, hasToken } from "@/lib/auth";
import { Lesson } from "@/types/lesson";
import { LessonProgress } from "@/types/taskProgress";
import { set } from "date-fns";
import { useEffect, useState } from "react";

export default function LessonPageClient() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [progressMap, setProgressMap] = useState<Record<string, LessonProgress>>({});
    const lessonGateway = GatewayFactory.instance.lessonGateway;
    const lessonProgressGateway = GatewayFactory.instance.lessonProgressGateway;

    useEffect(() => {
        const fetchData = async () => {
            const tokenExists = hasToken();
            setIsAuthenticated(tokenExists);

            // Fetch all lessons for display
            const lessons = await lessonGateway.getAll();

            const lessonsWithTasks = await Promise.all(
                lessons.map(async (lesson) => ({
                    ...lesson,
                    tasks: await lessonGateway.getTasksForLesson(lesson.lessonId)
                }))
            );
            setLessons(lessonsWithTasks);

            // If the user is not authenticated, skip fetching progress data
            if(!tokenExists) { return; }

            const user = await getCurrentUser();
            if(!user) { setIsAuthenticated(false); return; }
            
            // Fetch progress for all tasks for this lesson
            const progressList = await lessonProgressGateway.getAllLessonsProgress();
            const progressMap = Object.fromEntries(progressList.map((progress: LessonProgress) => [progress.lessonId, progress]));
            setProgressMap(progressMap);
        }
        fetchData();
    }, []);

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
                    <LessonCard key={lesson.lessonId} lesson={lesson} progress={ isAuthenticated ? progressMap[lesson.lessonId] : undefined} />
                    ))}
                </div>
            </div>
        </div>
    );
}