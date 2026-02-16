import { UserResponse } from "@/types/user";

export interface UserGateway {
    register(firstName: string, lastName: string, emailAddress: string, password: string): Promise<UserResponse>;
}