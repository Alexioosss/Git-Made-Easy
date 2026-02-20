import { DashboardData } from "@/types/dashboard";

export interface DashboardGateway {
    getDashboardData(): Promise<DashboardData>;
}