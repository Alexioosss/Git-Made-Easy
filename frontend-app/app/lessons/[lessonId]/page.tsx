import { Metadata } from "next";
import LessonDetailClient from "./LessonDetailPage";

export const metadata: Metadata = {
  title: "Lesson Details",
}

export default function LessonDetailPage({params}: {params: { lessonId: string }}) {
  return <LessonDetailClient lessonId={params.lessonId} />;
}