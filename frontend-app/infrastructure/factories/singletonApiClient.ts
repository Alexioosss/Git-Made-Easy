import { ApiClient } from "../gateways/ApiClient";
import { ApiClientFactory } from "./apiClientFactory";

let instance: ApiClient | null = null;

/**
 * Function to create a singleton instance of the ApiClient class, responsible for communicating with the external world / external systems
 * @returns Returns the singleton instance of the ApiClient class created via a factory
 */
export function getApiClient(): ApiClient {
    if(!instance) { instance = ApiClientFactory.createApiClient(); } // Only create an instance of the ApiClient if there is not already an existing one, avoids duplication and promotes centralised configuration of sensitive classes
    return instance;
}