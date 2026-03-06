import { GatewayFactory } from "@/config/GatewayFactory";

export async function getCurrentUser() {
    try { return await GatewayFactory.instance.authGateway.getCurrentUser(); }
    catch(err: any) {
        if(err?.status === 401) { return null; }
        throw new Error("SERVER_UNAVAILABLE");
    }
}

export async function logoutUser() {
    try {
        await GatewayFactory.instance.authGateway.logout();
        document.cookie = "access_token=; Max-Age=0; path=/;";
        return true;
    } catch { return null; }
}

export function hasToken() {
    return typeof document !== "undefined" && document.cookie.split(";").some((cookie) => cookie.trim().startsWith("access_token="));
}