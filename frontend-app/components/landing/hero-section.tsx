"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Terminal, ArrowRight } from "lucide-react";

export function HeroSection() {
  return (
    <section className="relative overflow-hidden px-4 py-16 sm:py-24 md:py-32">
      {/* Subtle grid background */}
      <div className="absolute inset-0 opacity-[0.03]"
        style={{ backgroundImage: "linear-gradient(hsl(var(--foreground)) 1px, transparent 1px), linear-gradient(90deg, hsl(var(--foreground)) 1px, transparent 1px)", backgroundSize: "60px 60px"}}/>

      <div className="relative mx-auto max-w-6xl">
        <div className="flex flex-col items-center text-center">
          <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-border bg-secondary px-3 py-1.5 text-xs text-muted-foreground sm:px-4 sm:text-sm">
            <Terminal className="h-3 w-3 text-primary sm:h-3.5 sm:w-3.5" />
            Interactive git learning platform
          </div>

          <h1 className="max-w-3xl text-balance text-3xl font-bold tracking-tight text-foreground sm:text-4xl md:text-6xl">
            Learn Git the
            <span className="text-primary"> right way.</span>
          </h1>

          <p className="mt-4 max-w-xl text-pretty text-base leading-relaxed text-muted-foreground sm:mt-6 sm:text-lg">
            Master version control through hands-on tasks. From basic commands
            to advanced workflows, practice real git commands at your own pace.
          </p>

          <div className="mt-8 flex w-full max-w-xs flex-col gap-4 sm:mt-10 sm:max-w-none sm:flex-row sm:items-center sm:justify-center sm:gap-4">
            <Button size="lg" asChild className="w-full py-6 text-base sm:w-auto sm:py-3 sm:text-sm">
              <Link href="/lessons" title="Visit lessons catalog" className="gap-2">
                Start Learning
                <ArrowRight className="h-4 w-4" />
              </Link>
            </Button>
            <Button variant="outline" size="lg" asChild className="w-full bg-transparent py-6 text-base sm:w-auto sm:py-3 sm:text-sm">
              <Link href="/register" title="Create an account">Create Account</Link>
            </Button>
          </div>

          {/* Terminal preview */}
          <div className="mt-12 w-full max-w-2xl sm:mt-16">
            <div className="overflow-hidden rounded-xl border border-border bg-card shadow-2xl shadow-primary/5">
              <div className="flex items-center gap-2 border-b border-border px-3 py-2.5 sm:px-4 sm:py-3">
                <div className="h-2.5 w-2.5 rounded-full bg-destructive/60 sm:h-3 sm:w-3" />
                <div className="h-2.5 w-2.5 rounded-full bg-warning/60 sm:h-3 sm:w-3" />
                <div className="h-2.5 w-2.5 rounded-full bg-primary/60 sm:h-3 sm:w-3" />
                <span className="ml-2 text-xs text-muted-foreground">
                  terminal
                </span>
              </div>
              <div className="overflow-x-auto p-4 font-mono text-xs sm:p-6 sm:text-sm">
                <div className="flex items-center gap-2 text-muted-foreground">
                  <span className="text-primary">$</span>
                  <span className="text-foreground">git init</span>
                </div>
                <div className="mt-2 text-muted-foreground">
                  Initialized empty Git repository in /project/.git/
                </div>
                <div className="mt-4 flex items-center gap-2 text-muted-foreground">
                  <span className="text-primary">$</span>
                  <span className="text-foreground">git add .</span>
                </div>
                <div className="mt-4 flex items-center gap-2 text-muted-foreground">
                  <span className="text-primary">$</span>
                  <span className="whitespace-nowrap text-foreground">
                    {'git commit -m "first commit"'}
                  </span>
                </div>
                <div className="mt-2 text-muted-foreground">
                  [main (root-commit) a1b2c3d] first commit
                </div>
                <div className="mt-1 text-muted-foreground">
                  {"3 files changed, 42 insertions(+)"}
                </div>
                <div className="mt-4 flex items-center gap-2">
                  <span className="text-primary">$</span>
                  <span className="inline-block h-4 w-2 animate-pulse bg-foreground" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}