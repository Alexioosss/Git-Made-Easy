import { HttpMethods } from "./HttpMethods";

export abstract class ApiClient {
    protected readonly BACKEND_URL: string;

    constructor(BACKEND_URL: string) {
        this.BACKEND_URL = BACKEND_URL.replace(/\/+$/, '') + '/'; // Remove trailing slashes from the given URL and add a single slash at the end
    }

    abstract ApiRequest<T>(path: string, method: HttpMethods, body?: any): Promise<T>;
}