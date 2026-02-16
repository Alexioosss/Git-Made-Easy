import React from "react"
import type { Metadata, Viewport } from "next";

import "./styles/globals.css";
import { Navbar } from "@/components/navbar";

export const metadata: Metadata = {
  title: {
    default: "Git Made Easy",
    template: "%s | Git Made Easy"
  },
  description: "Master Git version control through interactive lessons and hands-on tasks. From basic commands to advanced techniques."
};

export const viewport: Viewport = { themeColor: "#0a0c10" };

export default function RootLayout({children}: Readonly<{children: React.ReactNode}>) {
  return (
    <html lang="en">
      <body className="font-sans antialiased">
        <Navbar />
        {children}
      </body>
    </html>
  );
}