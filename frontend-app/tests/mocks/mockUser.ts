import { UserResponse } from "@/types/user";

export function createMockUser(overrides: Partial<UserResponse> = {}): UserResponse {
    return {
        id: "1",
        firstName: "John",
        lastName: "Doe",
        emailAddress: "john@example.com",
        ...overrides
    };
}