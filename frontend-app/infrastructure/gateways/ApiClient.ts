import { HttpMethods } from "./HttpMethods";

export abstract class ApiClient {
    protected readonly BACKEND_URL: string;

    constructor(BACKEND_URL: string) {
        this.BACKEND_URL = BACKEND_URL.replace(/\/+$/, ''); // Remove trailing slashes from the given URL
    }

    // An api request will require a path and an HTTP Method, a body is not required for a request
    // This sets the contract of what an api request will require when creating api client implementations
    abstract apiRequest<T>(path: string, method: HttpMethods, body?: any): Promise<T>;
}