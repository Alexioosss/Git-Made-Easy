import { UserResponse } from "@/types/user";
import { ApiClient } from "../ApiClient";
import { UserGateway } from "./UserGateway";
import { HttpMethods } from "../HttpMethods";

export class FetchUserGateway implements UserGateway {
    
    constructor(private apiClient: ApiClient) {}

    register(firstName: string, lastName: string, emailAddress: string, password: string): Promise<UserResponse> {
        return this.apiClient.ApiRequest("/users", HttpMethods.POST, { firstName, lastName, emailAddress, password });
    }
}