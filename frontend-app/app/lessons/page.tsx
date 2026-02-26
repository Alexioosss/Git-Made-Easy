import { Metadata } from "next";
import LessonPage from "./LessonPage";

export const metadata: Metadata = {
  title: "Lessons"
}

export default function LessonsPage() {
  return <LessonPage />;
}