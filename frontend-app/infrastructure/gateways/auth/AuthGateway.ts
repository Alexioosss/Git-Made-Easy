import { UserResponse } from "@/types/user";

export interface AuthGateway {
    login(emailAddress: string, password: string): Promise<string>;
    logout(): Promise<void>;
    refresh(): Promise<string>;
    getCurrentUser(): Promise<UserResponse>;
}