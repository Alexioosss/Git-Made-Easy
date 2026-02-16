import { HttpMethods } from "./HttpMethods";

export abstract class ApiClient {
    protected readonly BACKEND_URL: string;

    constructor(BACKEND_URL: string) {
        this.BACKEND_URL = BACKEND_URL.endsWith('/') ? BACKEND_URL : BACKEND_URL + '/';
    }

    abstract ApiRequest<T>(path: string, method: HttpMethods, body?: any): Promise<T>;
}