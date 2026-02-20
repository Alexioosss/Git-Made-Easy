import { DashboardData } from "@/types/dashboard";
import { ApiClient } from "../ApiClient";
import { HttpMethods } from "../HttpMethods";
import { DashboardGateway } from "./DashboardGateway";

export class FetchDashboardGateway implements DashboardGateway {

    constructor(private apiClient: ApiClient) {}

    async getDashboardData(): Promise<DashboardData> {
        return this.apiClient.apiRequest("/dashboard", HttpMethods.GET);
    }
}