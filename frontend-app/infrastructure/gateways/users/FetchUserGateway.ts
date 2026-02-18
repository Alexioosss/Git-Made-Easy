import { UserResponse } from "@/types/user";
import { ApiClient } from "../ApiClient";
import { UserGateway } from "./UserGateway";
import { HttpMethods } from "../HttpMethods";

export class FetchUserGateway implements UserGateway {
    
    constructor(private apiClient: ApiClient) {}

    register(firstName: string, lastName: string, emailAddress: string, password: string): Promise<UserResponse> {
        return this.apiClient.apiRequest("/users", HttpMethods.POST, { firstName, lastName, emailAddress, password });
    }

    getById(userId: string): Promise<UserResponse> {
        return this.apiClient.apiRequest(`/users/${userId}`, HttpMethods.GET);
    }

    getByEmailAddress(emailAddress: string): Promise<UserResponse> {
        return this.apiClient.apiRequest("/users", HttpMethods.GET, { emailAddress });
    }
}