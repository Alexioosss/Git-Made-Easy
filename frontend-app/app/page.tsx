import { HeroSection } from "@/components/landing/hero-section";
import { FeaturesSection } from "@/components/landing/features-section";
import { LessonsPreview } from "@/components/landing/lessons-preview";
import { CTASection } from "@/components/landing/cta-section";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Home",
}

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      <main>
        <HeroSection />
        <FeaturesSection />
        <LessonsPreview />
        <CTASection />
      </main>
      <footer className="border-t border-border py-8">
        <div className="mx-auto max-w-6xl px-4 text-center text-sm text-muted-foreground">
          Built for those who want to master Git.
        </div>
      </footer>
    </div>
  );
}