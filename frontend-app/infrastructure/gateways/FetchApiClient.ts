import { ApiClient } from "./ApiClient";
import { HttpMethods } from "./HttpMethods";

export class FetchApiClient extends ApiClient {

    async apiRequest<T>(path: string, method: HttpMethods, body?: any): Promise<T> {
        try {
            const response = await fetch(`${this.BACKEND_URL}${path}`, {
                method,
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: body ? JSON.stringify(body) : undefined,
                cache: "no-store"
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
                throw new Error(message);
            }
            
            return data;
        } catch(error: any) {
            throw new Error(error.message || "Unknown API Error");
        }
    }
}