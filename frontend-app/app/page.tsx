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
    </div>
  );
}