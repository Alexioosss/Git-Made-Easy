import { ApiClient } from "./ApiClient";
import { HttpMethods } from "./HttpMethods";

export class FetchApiClient extends ApiClient {
    private isRefreshing: boolean = false;
    private refreshPromise: Promise<void> | null = null;

    async apiRequest<T>(path: string, method: HttpMethods, body?: any, options?: RequestInit & { next?: { revalidate?: number | false } }): Promise<T> {
        try {
            return await this.performRequest<T>(path, method, body, options);
        } catch(error: any) {
            if(error.status === 401) {
                await this.refreshToken();
                return await this.performRequest<T>(path, method, body, options);
            }
            throw error;
        }
    }

    private async performRequest<T>(path: string, method: HttpMethods, body?: any, options?: RequestInit & { next?: { revalidate?: number | false } }): Promise<T> {
        const response = await fetch(`${this.BACKEND_URL}${path}`, {
            method,
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: body ? JSON.stringify(body) : undefined,
            cache: options?.cache ?? "no-store",
            next: options?.next
        });
        const contentType = response.headers.get("Content-Type") ?? "";

        let data: any = null;
        if(contentType.includes("application/json")) {
            data = await response.json();
        } else if(contentType.includes("text/plain")) {
            data = await response.text();
        }
        if(!response.ok) {
            const message = data?.error_message || data?.message || response.statusText || "Request Failed";
            const err: any = new Error(message);
            err.status = response.status;
            throw err;
        }
        
        return data;
    }

    private async refreshToken(): Promise<void> {
        if(this.isRefreshing) { return this.refreshPromise!; }
        this.isRefreshing = true;
        this.refreshPromise = fetch(`${this.BACKEND_URL}/auth/refresh`, {
            method: "POST",
            credentials: "include"
        }).then(response => {
            if(!response.ok) {
                throw new Error("Failed to refresh token");
            }
        }).finally(() => {
            this.isRefreshing = false;
            this.refreshPromise = null;
        });
        return this.refreshPromise;
    }
}