import { Metadata } from "next";
import LessonsPage from "./LessonPage";

export const metadata: Metadata = {
  title: "Lessons"
};

export default function AllLessonsPage() {
  return <LessonsPage />;
}