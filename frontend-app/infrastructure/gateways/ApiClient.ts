import { HttpMethods } from "./HttpMethods";

export abstract class ApiClient {
    protected readonly BACKEND_URL: string;

    constructor(BACKEND_URL: string) {
        this.BACKEND_URL = BACKEND_URL.replace(/\/+$/, ''); // Remove any trailing slashes from the given URL
    }

    // An api request will require a path and an HTTP Method, a body is not required for a request. There can be specified if the request can revalidate or cache its response
    // This sets the contract of what an api request will require when creating api client implementations, i.e. using different fetch/HTTP libraries
    abstract apiRequest<T>(path: string, method: HttpMethods, body?: any, options?: RequestInit & { next?: { revalidate?: number | false } }): Promise<T>;
}