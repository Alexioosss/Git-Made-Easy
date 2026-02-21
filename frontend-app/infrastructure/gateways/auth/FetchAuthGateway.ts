import { UserResponse } from "@/types/user";
import { ApiClient } from "../ApiClient";
import { HttpMethods } from "../HttpMethods";
import { AuthGateway } from "./AuthGateway";

export class FetchAuthGateway implements AuthGateway {
    
    constructor(private apiClient: ApiClient) {}

    login(emailAddress: string, password: string): Promise<string> {
        return this.apiClient.apiRequest("/auth/login", HttpMethods.POST, { email: emailAddress, password });
    }

    logout(): Promise<void> {
        return this.apiClient.apiRequest("/auth/logout", HttpMethods.POST);
    }

    refresh(): Promise<string> {
        return this.apiClient.apiRequest("/auth/refresh", HttpMethods.POST);
    }

    getCurrentUser(): Promise<UserResponse> {
        return this.apiClient.apiRequest("/auth/me", HttpMethods.GET);
    }
}