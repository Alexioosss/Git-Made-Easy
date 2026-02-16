import { ApiClient } from "../gateways/ApiClient";
import { ApiClientFactory } from "./apiClientFactory";

let instance: ApiClient | null = null;

export function getApiClient(): ApiClient {
    if (!instance) { instance = ApiClientFactory.createApiClient(); }
    return instance;
}