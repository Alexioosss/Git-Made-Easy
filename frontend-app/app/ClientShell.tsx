import { BackOnlineBanner } from "@/components/BackOnlineBanner";
import { ServerUnavailable } from "@/components/ServerUnavailable";

export function ClientShell({ children }: { children: React.ReactNode }) {
    return (
        <>
            <ServerUnavailable />
            <BackOnlineBanner />
            {children}
        </>
    );
}