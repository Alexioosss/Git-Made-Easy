import { Metadata } from "next";
import LogoutClient from "./LogoutClient";

export const metadata: Metadata = {
    title: "Logging out..."
};

export default function LogoutPage() {
    return (
        <LogoutClient />
    );
}