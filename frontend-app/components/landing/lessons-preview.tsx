import Link from "next/link";
import { mockLessons } from "@/lib/mock-data";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";
import { GatewayFactory } from "@/config/GatewayFactory";

export async function LessonsPreview() {
  const lessonsGateway = GatewayFactory.instance.lessonGateway;
  const lessons = await lessonsGateway.getAll();

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
          <Link href="/lessons" title="View lessons catalog" className="hidden items-center gap-1 text-sm text-primary transition-colors hover:text-primary/80 sm:flex">
            View all lessons
            <ArrowRight className="h-3.5 w-3.5" />
          </Link>
        </div>

        <div className="grid gap-4 sm:grid-cols-2">
          {lessons.map((lesson) => {
            const difficultyDistribution = lesson.tasks.reduce((acc, task) => {acc[task.difficulty] = (acc[task.difficulty] || 0) + 1; return acc; }, {} as Record<string, number>);

            return (
              <Link key={lesson.lessonId} href={`/lessons/${lesson.lessonId}`}
                className="group flex flex-col rounded-xl border border-border bg-card p-5 transition-all hover:border-primary/30 hover:shadow-lg hover:shadow-primary/5 sm:p-6">
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
                  <span className="text-xs text-muted-foreground">
                    {lesson.tasks.length} tasks
                  </span>
                  <span className="text-muted-foreground/30">|</span>
                  {Object.entries(difficultyDistribution).map(
                    ([difficulty, count]) => (
                      <Badge key={difficulty} variant={difficulty.toLowerCase() as "easy" | "medium" | "hard"} className="`text-xs`">
                        {count} {difficulty.toLowerCase()}
                      </Badge>
                    )
                  )}
                </div>
              </Link>
            );
          })}
        </div>

        {/* Mobile "View all" button */}
        <div className="mt-6 flex justify-center sm:hidden">
          <Button variant="outline" asChild className="w-full bg-transparent" title="View lessons catalog">
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