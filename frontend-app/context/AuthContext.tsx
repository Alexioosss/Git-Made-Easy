"use client"

import { getCurrentUser, logoutUser } from "@/lib/auth";
import { syncProgressFromTemporaryStorage } from "@/lib/syncProgressFromTemporaryStorage";
import { isAbsoluteUrl } from "next/dist/shared/lib/utils";
import { createContext, ReactNode, useContext, useEffect, useRef, useState } from "react";

export type AuthContextType = {
    user: any | null;
    isAuthenticated: boolean;
    refreshUser: () => Promise<void>;
    logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<any | null>(null);
    const { isAuthenticated } = { isAuthenticated: !!user };

    async function refreshUser() { const user = await getCurrentUser(); setUser(user ?? null); }

    async function logout() { await logoutUser(); setUser(null); }

    useEffect(() => { refreshUser(); }, []);

    const prevAuthRef = useRef<boolean>(false);
    useEffect(() => {
        if(isAuthenticated && !prevAuthRef.current) { // If the user has logged in
            syncProgressFromTemporaryStorage(); // Save any progress made while logged out (if any)
        }
        prevAuthRef.current = isAuthenticated;
    }, [isAuthenticated]);

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: !!user, refreshUser, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if(!context) { throw new Error("useAuth must be used inside AuthProvider"); }
    return context;
}