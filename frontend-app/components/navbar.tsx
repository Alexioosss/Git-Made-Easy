"use client";

import { useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { GitBranch, User, LogOut, LayoutDashboard, Menu, X } from "lucide-react";

export function Navbar() {
  const isAuthenticated = false;
  const user = { firstName: "John" };
  const logout = () => { console.log("Logging out..."); };
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState<boolean>(false);

  return (
    <header className="sticky top-0 z-50 border-b border-border bg-background/80 backdrop-blur-md">
      <div className="mx-auto flex h-14 max-w-6xl items-center justify-between px-4 sm:h-16">
        <Link href="/" title="Go to home page" className="flex items-center gap-2">
          <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary">
            <GitBranch className="h-6 w-6 text-primary-foreground" />
          </div>
          <span className="text-base font-semibold text-foreground sm:text-2xl">
            GitMadeEasy
          </span>
        </Link>

        {/* Desktop nav */}
        <nav className="hidden items-center gap-6 md:flex">
          <Link href="/lessons" title="View all lessons" className="text-2xl text-foreground transition-colors hover:text-foreground hover:scale-125 transition-transform duration-300">
            Lessons
          </Link>
          {isAuthenticated && (
            <Link href="/dashboard" className="text-2xl text-muted-foreground transition-colors hover:text-foreground hover:scale-125 transition-transform duration-300">
              Dashboard
            </Link>
          )}
        </nav>

        <div className="flex items-center gap-2">
          {isAuthenticated ? (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="ghost" className="flex items-center gap-2 text-muted-foreground hover:text-foreground" title="Open the user menu">
                  <div className="flex h-7 w-7 items-center justify-center rounded-full bg-primary/20 text-primary">
                    <User className="h-3.5 w-3.5" />
                  </div>
                  <span className="hidden text-sm sm:inline">
                    {user?.firstName}
                  </span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-48 bg-card text-card-foreground">
                <DropdownMenuItem asChild>
                  <Link href="/dashboard" className="flex items-center gap-2 py-3 text-xl hover:text-white transition-colors duration-400">
                    <LayoutDashboard className="h-5 w-5" />
                    Dashboard
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem onClick={logout} className="flex items-center gap-2 py-3 text-destructive focus:text-destructive text-xl hover:text-white transition-colors duration-400">
                  <LogOut className="h-5 w-5" />
                  Sign Out
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          ) : (
            <div className="hidden items-center gap-2 sm:flex">
              <Button asChild className="text-xl px-5 py-2 hover:text-white transition-colors duration-400" title="Sign in to your account">
                <Link href="/login">Sign In</Link>
              </Button>
              <Button asChild className="text-xl px-5 py-2 hover:text-white transition-colors duration-400" title="Create a new account">
                <Link href="/register">Get Started</Link>
              </Button>
            </div>
          )}

          {/* Mobile hamburger */}
          <button type="button" onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            className="flex h-9 w-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground md:hidden"
            aria-label={isMobileMenuOpen ? "Close menu" : "Open menu"}>
            {isMobileMenuOpen ? ( <X className="h-5 w-5" /> ) : ( <Menu className="h-5 w-5" /> )}
          </button>
        </div>
      </div>

      {/* Mobile menu */}
      {isMobileMenuOpen && (
        <div className="border-t border-border bg-background px-4 pb-4 pt-3 md:hidden">
          <nav className="flex flex-col gap-1">
            <Link href="/lessons" onClick={() => setIsMobileMenuOpen(false)} className="rounded-lg px-3 py-2.5 text-sm text-foreground transition-colors hover:bg-secondary">
              Lessons
            </Link>
            {isAuthenticated && (
              <Link href="/dashboard" onClick={() => setIsMobileMenuOpen(false)} className="rounded-lg px-3 py-2.5 text-sm text-foreground transition-colors hover:bg-secondary">
                Dashboard
              </Link>
            )}
          </nav>
          {!isAuthenticated && (
            <div className="mt-3 flex flex-col gap-2 border-t border-border pt-3">
              <Button variant="outline" asChild className="w-full bg-transparent">
                <Link href="/login" onClick={() => setIsMobileMenuOpen(false)} >
                  Sign In
                </Link>
              </Button>
              <Button asChild className="w-full">
                <Link href="/register" onClick={() => setIsMobileMenuOpen(false)}>
                  Get Started
                </Link>
              </Button>
            </div>
          )}
        </div>
      )}
    </header>
  );
}