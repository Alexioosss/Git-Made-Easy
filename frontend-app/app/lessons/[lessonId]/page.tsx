import { Metadata } from "next";
import LessonDetail from "./LessonDetail";
import { GatewayFactory } from "@/config/GatewayFactory";
import { safeCallWrapper } from "@/lib/safeCallWrapper";
import { LessonProgress } from "@/types/taskProgress";

export const metadata: Metadata = {
  title: "Lesson Details",
}

export default async function LessonDetailPage({ params }: { params: Promise<{ lessonId: string }>}) {
  const { lessonId } = await params;

  const lessonGateway = GatewayFactory.instance.lessonGateway;
  const progressGateway = GatewayFactory.instance.lessonProgressGateway;
  
  const lessonResponse = await safeCallWrapper(() => lessonGateway.getById(lessonId));
  if(!lessonResponse.ok || !lessonResponse.data) {
    return (
      <div className="min-h-[calc(100dvh-3.5rem)] sm:min-h-[calc(100dvh-4rem)] flex items-center justify-center px-6">
        <p className="text-center text-foreground text-2xl">
          Could not load lesson.
          <span className="block sm:inline"> Please try again later.</span>
        </p>
      </div>
    );
  }

  const lesson = lessonResponse.data;
  const nextLesson = (await safeCallWrapper(() => lessonGateway.getNextLesson(lessonId))).data;

  let progress: LessonProgress | undefined = undefined;
  try { progress = await progressGateway.getLessonProgress(lessonId); }
  catch {}

  return <LessonDetail lesson={lesson} progress={progress} nextLesson={nextLesson} />;
}