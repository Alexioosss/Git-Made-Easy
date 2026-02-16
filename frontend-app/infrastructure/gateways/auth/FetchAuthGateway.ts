import { ApiClient } from "../ApiClient";
import { HttpMethods } from "../HttpMethods";
import { AuthGateway } from "./AuthGateway";

export class FetchAuthGateway implements AuthGateway {
    
    constructor(private apiClient: ApiClient) {}

    login(emailAddress: string, password: string): Promise<string> {
        return this.apiClient.ApiRequest("/auth/login", HttpMethods.POST, { emailAddress, password });
    }

    logout(): Promise<void> {
        return this.apiClient.ApiRequest("/auth/logout", HttpMethods.POST);
    }
}