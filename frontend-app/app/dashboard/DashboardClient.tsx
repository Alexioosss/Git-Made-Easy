"use client"

import { DashboardLessons } from "@/components/dashboard/dashboard-lessons";
import { DashboardStats } from "@/components/dashboard/dashboard-stats";
import { RecentActivity } from "@/components/dashboard/recent-activity";
import { GatewayFactory } from "@/config/GatewayFactory";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default async function DashboardClient() {
    const router = useRouter();
    const isLoading = false;
    const isAuthenticated = true;
    const dashboardGateway = GatewayFactory.instance.dashboardGateway;
    const dashboardData = await dashboardGateway.getDashboardData();
    console.log(dashboardData);

    useEffect(() => {
        if(!isLoading && !isAuthenticated) {
        router.push("/login");
        }
    }, [isLoading, isAuthenticated, router]);

    if(isLoading || !isAuthenticated) {
        return (
        <div className="flex min-h-screen items-center justify-center bg-background">
            <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary border-t-transparent" />
        </div>
        );
    }

    return (
        <div className="min-h-screen bg-background">
            <div className="mx-auto max-w-6xl px-4 py-8 sm:py-12">
                <div className="mb-8 sm:mb-10">
                <h1 className="text-2xl font-bold text-foreground sm:text-3xl">
                    Welcome back, {dashboardData.firstName}
                </h1>
                <p className="mt-2 text-sm text-muted-foreground sm:text-base">
                    Here can be found an overview of your learning progress so far.
                </p>
                </div>

                <DashboardStats />

                <div className="mt-6 flex flex-col gap-6 sm:mt-8 sm:gap-8 lg:grid lg:grid-cols-3">
                    <div className="lg:col-span-2">
                        <DashboardLessons />
                    </div>
                    <div>
                        <RecentActivity />
                    </div>
                </div>
            </div>
        </div>
    );
}