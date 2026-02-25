import { ApiClient } from "@/infrastructure/gateways/ApiClient";

export function createMockApiClient(): jest.Mocked<ApiClient> {
    return {
        apiRequest: jest.fn()
    } as unknown as jest.Mocked<ApiClient>
};