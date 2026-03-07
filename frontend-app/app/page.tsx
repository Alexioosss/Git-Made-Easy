import { IntroSection } from "@/components/landing/hero-section";
import { HighlightsSection } from "@/components/landing/features-section";
import { LessonsPreview } from "@/components/landing/lessons-preview";
import { GetStartedSection } from "@/components/landing/get-started";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Home | Git Made Easy"
};

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      <main>
        <IntroSection />
        <HighlightsSection />
        <LessonsPreview />
        <GetStartedSection />
      </main>
    </div>
  );
}