import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";
import { GatewayFactory } from "@/config/GatewayFactory";
import { Lesson } from "@/types/lesson";
import { safeCallWrapper } from "@/lib/safeCallWrapper";

export async function LessonsPreview() {
  const lessonsGateway = GatewayFactory.instance.lessonGateway;
  const response = await safeCallWrapper(() => lessonsGateway.getAllLessons())

  if(!response.ok || !response.data) {
    return (
      <section className="border-t border-border px-4 py-14 sm:py-20">
        <div className="mx-auto max-w-6xl text-center">
          <h2 className="text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
            Lesson Catalog
          </h2>
          <p className="mt-2 text-md text-muted-foreground">
            Looks like the lessons are temporarily unavailable. Please try again in a moment.
          </p>
        </div>
      </section>
    );
  }

  const lessons = response.data as Lesson[];

  return (
    <section className="border-t border-border px-4 py-14 sm:py-20">
      <div className="mx-auto max-w-6xl">
        <div className="mb-8 flex flex-col gap-4 sm:mb-12 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 className="text-balance text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
              Lesson Catalog
            </h2>
            <p className="mt-2 text-sm text-muted-foreground sm:mt-3 sm:text-base">
              Start from the basics or jump to what you need.
            </p>
          </div>
          <Link href="/lessons" title="View lessons catalog" className="hidden items-center gap-1 text-md text-primary transition-colors hover:font-bold sm:flex group">
            View all lessons
            <ArrowRight className="h-3.5 w-3.5 arrow-hover drop-shadow-[0_0_4px_hsl(var(--primary))]" />
          </Link>
        </div>

        <div className="grid gap-4 sm:grid-cols-2">
          {lessons.slice(0, 4).map((lesson) => { // Show a maximum of 4 lessons in the home page lessons preview / catalog

            return (
              <Link key={lesson.lessonId} href={`/lessons/${lesson.lessonId}`} title={`Lesson ${lesson.lessonOrder} - ${lesson.title}`}
                className="group flex flex-col rounded-xl border border-border bg-card p-5 transition-all
                hover:-translate-y-2 hover:shadow-xl dark:hover:shadow-[0_0_30px_rgba(255,255,255,0.15)] hover:border-primary/100 sm:p-6">
                <div className="mb-3 flex items-center gap-3">
                  <span className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-secondary text-sm font-semibold text-secondary-foreground">
                    {lesson.lessonOrder}
                  </span>
                  <h3 className="font-semibold text-card-foreground group-hover:text-primary transition-colors">
                    {lesson.title}
                  </h3>
                </div>
                <p className="mb-4 flex-1 text-sm leading-relaxed text-muted-foreground">
                  {lesson.description}
                </p>
                <div className="flex flex-wrap items-center gap-2">
                  {lesson.taskIds.length === 0 ? (<span className="text-xs text-muted-foreground">No tasks yet</span>) : (
                    <span className="text-xs text-muted-foreground">
                      {lesson.taskIds.length}{" "} {lesson.taskIds.length === 1 ? "task" : "tasks"}{""}
                    </span>
                  )}
                </div>
              </Link>
            );
          })}
        </div>

        {/* Mobile "View all" button */}
        <div className="mt-6 flex justify-center sm:hidden">
          <Button variant="outline" asChild className="w-full bg-transparent hover:font-bold" title="View lessons catalog">
            <Link href="/lessons" className="gap-2">
              View all lessons
              <ArrowRight className="h-4 w-4" />
            </Link>
          </Button>
        </div>
      </div>
    </section>
  );
}