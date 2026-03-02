import { GatewayFactory } from "@/config/GatewayFactory";

export async function getCurrentUser() {
    try { return await GatewayFactory.instance.authGateway.getCurrentUser(); }
    catch { return null; }
}

export async function logoutUser() {
    try {
        await GatewayFactory.instance.authGateway.logout();
        document.cookie = "access_token=; Max-Age=0; path=/;";
        return true;
    } catch { return false; }
}

export function hasToken() {
    return typeof document !== "undefined" && document.cookie.split(";").some((cookie) => cookie.trim().startsWith("access_token="));
}