import { Metadata } from "next";
import DashboardClient from "./DashboardClient";

export const metadata: Metadata = {
  title: "Dashboard"
}

export default async function DashboardPage() {
  return <DashboardClient />;
}