"use client"

import { LessonCard } from "@/components/lessons/lesson-card";
import { GatewayFactory } from "@/config/GatewayFactory";
import { mockLessonProgress, mockLessons } from "@/lib/mock-data";
import { Lesson } from "@/types/lesson";
import { LessonProgress } from "@/types/taskProgress";
import { useEffect, useState } from "react";

export default function LessonPageClient() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [lessonProgress, setLessonProgress] = useState<LessonProgress>();

    const lessonGateway = GatewayFactory.instance.lessonGateway;
    const lessonProgressGateway = GatewayFactory.instance.lessonProgressGateway;

    useEffect(() => {
        const fetchLessonsAndProgresses = async () => {
            // setLessons(await lessonGateway.getAll());
            // setLessonProgress(await lessonProgressGateway.getLessonProgress());
        }
        fetchLessonsAndProgresses();
        setIsAuthenticated(true);
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
                    {mockLessons.map((lesson) => (
                    <LessonCard key={lesson.lessonId} lesson={lesson} progress={ isAuthenticated ? mockLessonProgress[lesson.lessonId] : undefined} />
                    ))}
                </div>
            </div>
        </div>
    );
}