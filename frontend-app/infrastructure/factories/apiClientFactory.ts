import { FetchApiClient } from "../gateways/FetchApiClient";

/**
 * Factory class to create an instance of the ApiClient class, providing the required URL for instantiation
 * It is solely responsible for creating an instance of the ApiClient to decouple logic and improve testability and maintainability
 */
export class ApiClientFactory {
    static createApiClient() {
        const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';
        return new FetchApiClient(BASE_URL);
    }
}