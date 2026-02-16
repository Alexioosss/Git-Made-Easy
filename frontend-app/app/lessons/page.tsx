import { Metadata } from "next";
import LessonPageClient from "./LessonPageClient";

export const metadata: Metadata = {
  title: "Lessons"
}

export default function LessonsPage() {
  return <LessonPageClient />;
}