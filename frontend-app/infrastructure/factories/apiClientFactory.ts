import { FetchApiClient } from "../gateways/FetchApiClient";

export class ApiClientFactory {
    static createApiClient() {
        const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';
        return new FetchApiClient(BASE_URL);
    }
}