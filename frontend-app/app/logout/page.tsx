import { Metadata } from "next";
import LogoutClient from "./LogoutClient";

export const metadata: Metadata = {
    title: "Logout"
};

export default function LogoutPage() {
    return ( <LogoutClient /> );
}