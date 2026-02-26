"use client"

import LoadingSpinner from "@/components/LoadingSpinner";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function LogoutClient() {
    const { logout } = useAuth();
    const router = useRouter();

    useEffect(() => {
        logout().then(() => { router.push("/"); });
    }, [logout, router]);

    return ( <LoadingSpinner message="Logging out, please wait..." /> );
}