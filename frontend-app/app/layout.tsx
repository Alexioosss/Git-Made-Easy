import React from "react"
import type { Metadata, Viewport } from "next";

import "./styles/globals.css";
import { Navbar } from "@/components/navbar/navbar";
import { ScrollToTop } from "./scroll-to-top";
import { AuthProvider } from "@/context/AuthContext";
import { ThemeProvider } from "@/components/theme/theme-provider";

const DEFAULT_TITLE: string = "Git Made Easy" as const;

export const metadata: Metadata = {
  title: {
    default: DEFAULT_TITLE,
    template: `%s | ${DEFAULT_TITLE}`
  },
  description: "Master Git version control through interactive lessons and hands-on tasks. From basic commands to advanced techniques."
};

export const viewport: Viewport = { themeColor: "#0a0c10" };

export default function RootLayout({children}: Readonly<{children: React.ReactNode}>) {
  return (
    <html lang="en" className="dark" suppressHydrationWarning>
      <body className="font-sans antialiased min-h-screen flex flex-col">
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
          <AuthProvider>
            <ScrollToTop /> {/* Script to automatically scroll the page to the top on page change, since the pages remain to the last scrolled position  */}
            <Navbar /> {/* Show the navbar on every single page */}
            <main className="flex-1">
              {children}
            </main>
          </AuthProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}