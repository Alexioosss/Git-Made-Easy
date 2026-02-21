"use client"

import { getCurrentUser, logoutUser } from "@/lib/auth";
import { createContext, ReactNode, useContext, useEffect, useState } from "react";

type AuthContextType = {
    user: any | null;
    isAuthenticated: boolean;
    refreshUser: () => Promise<void>;
    logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<any | null>(null);

    async function refreshUser() {
        const user = await getCurrentUser();
        setUser(user ?? null);
    }

    async function logout() {
        await logoutUser();
        setUser(null);
    }

    useEffect(() => {
        refreshUser();
    }, []);

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