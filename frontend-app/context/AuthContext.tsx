"use client"

import { getCurrentUser, logoutUser } from "@/lib/auth";
import { syncProgressFromTemporaryStorage } from "@/lib/syncProgressFromTemporaryStorage";
import { syncProgressFromBackendStorage } from "@/lib/syncProgressFromBackendStorage";
import { createContext, ReactNode, useContext, useEffect, useRef, useState } from "react";

export type AuthContextType = {
    user: any | null;
    isAuthenticated: boolean;
    isLoadingUser: boolean;
    isServerAvailable: boolean;
    refreshUser: () => Promise<void>;
    logout: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<any | null>(null);
    const [isLoadingUser, setIsLoadingUser] = useState<boolean>(true);
    const [isServerAvailable, setIsServerAvailable] = useState<boolean>(true);
    const isAuthenticated = !!user;

    async function refreshUser() { const user = await getCurrentUser(); setUser(user ?? null); }

    async function logout() {
        try {
            await logoutUser();
        } catch(err: any) {
            if(err.status !== 401) { throw err; }
        }
        setUser(null);
    }

    useEffect(() => { // Effect to run once only
        let isMounted = true;
        async function check() {
            try {
                const user = await getCurrentUser(); // Try and retrieve the current user
                setUser(user);
                setIsServerAvailable(true); // If the user was retrieved, the server responded, thus is available
            } catch(err: any) {
                if(err.status === 401) { // If the user was not retrieved, and the error returned is 401, the user is unauthorized to make this request, most likely a missing token
                    setUser(null);
                    return;
                }
                if(!isMounted) { return; }
                if(err.message === "SERVER_UNAVAILABLE") { setIsServerAvailable(false); } // If the server returns a response that contains the default HTTP message of 'SERVER_UNAVAILABLE', it's because the server cannot be reached, i.e. offline
            } finally {
                setIsLoadingUser(false);
            }
        }
        check();
        const interval = setInterval(check, 7000); // Set an interval of 7 seconds to run the check for the server running, if it is not running, this can make the UI rendering dynamic and more accurate, avoiding breakage and issues
        return () => { isMounted = false; clearInterval(interval); }
    }, []);

    const prevAuthRef = useRef<boolean>(false); // Previous authentication state of the current user
    useEffect(() => {
        async function syncAfterLoggingIn() {
            const wasAuthenticated = prevAuthRef.current; // Persist the current authentication state of the user
            prevAuthRef.current = isAuthenticated; // Reference the new isAuthenticated state of the user, which comes from the fetching of the current user from the server
            if(!isAuthenticated) { return; }
            if(isAuthenticated && !wasAuthenticated) { // If the user has logged in after being logged out
                await new Promise(r => setTimeout(r, 50)); // wait for the login request to be completed and for the token cookie to be saved
                await syncProgressFromTemporaryStorage(); // Save any progress made while logged out (if any) to the database
                await syncProgressFromBackendStorage(); // Save any progress persisted in the data store to the local storage for dynamic, up-to-date rendering of the user's progresses
            }
        }
        syncAfterLoggingIn();
    }, [isAuthenticated]); // Run this effect every time that the user changes its authentication state, i.e. logged in -> logged out and vice-versa

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: !!user, isLoadingUser, isServerAvailable, refreshUser, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if(!context) { throw new Error("useAuth must be used inside AuthProvider"); }
    return context;
}