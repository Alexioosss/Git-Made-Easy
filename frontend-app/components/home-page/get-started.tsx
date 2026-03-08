import Link from "next/link";
import { Button } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";

export function GetStartedSection() {
  return (
    <section className="border-t border-border px-4 py-14 sm:py-20">
      <div className="mx-auto max-w-6xl text-center">
        <h2 className="text-balance text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
          Ready to level up your Git skills?
        </h2>
        <p className="mt-3 text-sm text-muted-foreground sm:mt-4 sm:text-base">
          No sign-up required. Start practicing Git commands right now.
        </p>
        <div className="mt-6 sm:mt-8">
          <Button size="lg" asChild className="w-full sm:w-auto" title="View lessons catalog">
            <Link href="/lessons" className="gap-2 group">
              Browse Lessons
              <ArrowRight className="h-4 w-4 arrow-hover" />
            </Link>
          </Button>
        </div>
      </div>
    </section>
  );
}