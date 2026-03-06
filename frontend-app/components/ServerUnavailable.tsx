"use client"

import { useAuth } from "@/context/AuthContext";

export function ServerUnavailable() {
    const isServerAvailable = useAuth().isServerAvailable;
    if(isServerAvailable) { return null; }
    return (
        <div className="sticky top-[56px] md:top-[64px] z-50 w-full bg-red-600 text-white text-center py-2 text-sm font-medium">
            We're having trouble connecting to the server. Some features may still work.
        </div>
    );
}