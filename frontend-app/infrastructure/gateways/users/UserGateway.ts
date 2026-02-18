import { UserResponse } from "@/types/user";

export interface UserGateway {
    register(firstName: string, lastName: string, emailAddress: string, password: string): Promise<UserResponse>;
    getById(userId: string): Promise<UserResponse>;
    getByEmailAddress(emailAddress: string): Promise<UserResponse>;
}