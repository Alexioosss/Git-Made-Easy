import { IntroSection } from "@/components/home-page/hero-section";
import { HighlightsSection } from "@/components/home-page/features-section";
import { LessonsPreview } from "@/components/home-page/lessons-preview";
import { GetStartedSection } from "@/components/home-page/get-started";
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