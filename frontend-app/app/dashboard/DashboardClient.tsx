"use client"

import { DashboardLessons } from "@/components/dashboard/dashboard-lessons";
import { DashboardStats } from "@/components/dashboard/dashboard-stats";
import { RecentActivity } from "@/components/dashboard/recent-activity";
import LoadingSpinner from "@/components/LoadingSpinner";
import { GatewayFactory } from "@/config/GatewayFactory";
import { getCurrentUser } from "@/lib/auth";
import { DashboardData } from "@/types/dashboard";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function DashboardClient() {
    const [data, setData] = useState<DashboardData>();
    const [isLoading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
    const router = useRouter();

    useEffect(() => {
        async function loadDashboard() {
            const user = await getCurrentUser();
            setIsLoggedIn(!!user);
            if(!user) { router.push("/login"); return; }
            setIsLoggedIn(true);
            try {
                const response = await GatewayFactory.instance.dashboardGateway.getDashboardData();
                setData(response);
            } catch(err: any) {
                setError(err.message || "An error occurred while loading the dashboard.");
            } finally {
                setLoading(false);
            }
        }
        loadDashboard();
    }, [router]);

    const activities = (data?.tasksProgress ?? []).filter(progress => progress.status !== "NOT_STARTED").map(progress => {
        const lesson = data?.lessons.find(l => l.lessonId === progress.lessonId);
        return {
            taskId: progress.taskId,
            taskTitle: progress.taskTitle,
            lessonTitle: lesson?.title ?? "Unknown Lesson Title",
            attempts: progress.attempts,
            completed: progress.status === "COMPLETED",
            date: progress.completedAt || progress.startedAt
        };
    }).sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());

    if(isLoading) {
        return ( <LoadingSpinner message={isLoggedIn ? "Your personalised dashboard will be displayed soon." : "If your dashboard does not load, please log in again."} /> );
    }

    if(error) {
        return (
        <div className="flex min-h-screen items-center justify-center bg-background">
            <p className="text-xl text-destructive">{error ? error[0].toUpperCase() + error.slice(1) : ""}</p>
        </div>
        );
    }

    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-6xl px-4 py-8 sm:py-12">
                <div className="mb-8 sm:mb-10">
                    <h1 className="text-2xl font-bold text-foreground sm:text-3xl">
                        Welcome back{data?.firstName && `, ${data.firstName}!`}
                    </h1>
                    <p className="mt-2 text-sm text-muted-foreground sm:text-base">
                        Here can be found an overview of your learning progress so far.
                    </p>
                </div>

                {data && <DashboardStats data={data} />}

                {data && (
                    <div className="mt-6 flex flex-col gap-6 sm:mt-8 sm:gap-8 lg:grid lg:grid-cols-3">
                        <div className="lg:col-span-2">
                            <DashboardLessons lessons={data.lessons} />
                        </div>
                        <div>
                            <RecentActivity activities={activities} />
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}