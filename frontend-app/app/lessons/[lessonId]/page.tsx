import { Metadata } from "next";
import LessonDetailClient from "./LessonDetailPage";

export const metadata: Metadata = {
  title: "Lesson Details",
}

export default async function LessonDetailPage({params}: {params: Promise<{ lessonId: string }>}) {
  const { lessonId } = await params;
  return <LessonDetailClient lessonId={lessonId} />;
}