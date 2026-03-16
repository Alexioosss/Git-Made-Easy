import { AuthToken } from "@/types/auth";
import { UserResponse } from "@/types/user";

export interface AuthGateway {
    login(emailAddress: string, password: string): Promise<AuthToken>;
    logout(): Promise<void>;
    refresh(): Promise<string>;
    getCurrentUser(): Promise<UserResponse>;
    resendVerificationEmail(emailAddress: string): Promise<void>;
}