import React from "react"
import type { Metadata, Viewport } from "next";

import "./styles/globals.css";
import { Navbar } from "@/components/navbar/navbar";
import { ScrollToTop } from "./scroll-to-top";
import { AuthProvider } from "@/context/AuthContext";
import { ThemeProvider } from "@/components/theme/theme-provider";
import { ClientShell } from "./ClientShell";

const DEFAULT_TITLE: string = "Git Made Easy";

export const metadata: Metadata = {
  title: {
    default: DEFAULT_TITLE,
    template: `%s | ${DEFAULT_TITLE}`
  },
  description: "Master Git version control through interactive lessons and hands-on tasks. From basic commands to advanced techniques. Your progress is saved automatically, and can be accessed at any time."
};

// Automatically sets the <media> tag for the page based on color scheme preference, system or user-preference dictated
export const viewport: Viewport = {
  themeColor: [
    { media: "(prefers-color-scheme: light)", color: "#FFFFFF" },
    { media: "(prefers-color-scheme: dark)", color: "#000000" }
  ]
};

export default function RootLayout({children}: Readonly<{children: React.ReactNode}>) {
  return (
    <html lang="en" className="dark" suppressHydrationWarning>
      <body className="font-sans antialiased min-h-screen flex flex-col">
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem> {/* Wrap the entire website with the theme provider, handling system, dark and light modes across pages */}
          <AuthProvider> {/* Utility class to provide quick-access methods, i.e. isUserAuthenticated, isServerAvailable, to handle authenticated logic easier across pages */}
            <ScrollToTop /> {/* Script to automatically scroll to the top of the page on page change, since the pages remain in the last scrolled position  */}
            <Navbar /> {/* Show the navbar on every single page across the entire website */}

            <ClientShell> {/* Wrapper to show a banner just underneath the Navbar to inform the user that the server is down, or subsequently, to inform the user that the server is back online */}
              <main className="flex-1">
                {children}
              </main>
            </ClientShell>
          </AuthProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}