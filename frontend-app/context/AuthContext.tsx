"use client"

import { getCurrentUser, logoutUser } from "@/lib/auth";
import { syncProgressFromTemporaryStorage } from "@/lib/syncProgressFromTemporaryStorage";
import { isAbsoluteUrl } from "next/dist/shared/lib/utils";
import { createContext, ReactNode, useContext, useEffect, useRef, useState } from "react";

export type AuthContextType = {
    user: any | null;
    isAuthenticated: boolean;
    isServerAvailable: boolean;
    refreshUser: () => Promise<void>;
    logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<any | null>(null);
    const [isServerAvailable, setIsServerAvailable] = useState<boolean>(true);
    const isAuthenticated = !!user;

    async function refreshUser() { const user = await getCurrentUser(); setUser(user ?? null); }

    async function logout() { await logoutUser(); setUser(null); }

    useEffect(() => {
        let isMounted = true;
        async function check() {
            try {
                const user = await getCurrentUser();
                setUser(user);
                setIsServerAvailable(true);
            } catch(err: any) {
                if(!isMounted) { return; }
                if(err.message === "SERVER_UNAVAILABLE") {
                    setIsServerAvailable(false);
                }
            }
        }
        check();
        const interval = setInterval(check, 7000); // Set an interval, to run the check for the server running, of 7 seconds
        return () => { isMounted = false; clearInterval(interval); }
    }, []);

    const prevAuthRef = useRef<boolean>(false);
    useEffect(() => {
        if(isAuthenticated && !prevAuthRef.current) { // If the user has logged in
            syncProgressFromTemporaryStorage(); // Save any progress made while logged out (if any)
        }
        prevAuthRef.current = isAuthenticated;
    }, [isAuthenticated]);

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: !!user, isServerAvailable, refreshUser, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if(!context) { throw new Error("useAuth must be used inside AuthProvider"); }
    return context;
}