import { BookOpen, Zap, BarChart3, Users } from "lucide-react";

const features = [
  {
    icon: BookOpen,
    title: "Structured Lessons",
    description:
      "Progress through carefully ordered lessons, from the basics to advanced Git techniques.",
  },
  {
    icon: Zap,
    title: "Hands-On Tasks",
    description:
      "Practice real Git commands with interactive tasks. Get instant feedback on your answers.",
  },
  {
    icon: BarChart3,
    title: "Track Progress",
    description:
      "Sign in to save your progress. See which tasks you have completed and how many attempts it took.",
  },
  {
    icon: Users,
    title: "No Account Required",
    description:
      "Start learning immediately. Create an account only when you want to save your progress.",
  },
];

export function FeaturesSection() {
  return (
    <section className="border-t border-border px-4 py-14 sm:py-20">
      <div className="mx-auto max-w-6xl">
        <div className="mb-10 text-center sm:mb-12">
          <h2 className="text-balance text-xl font-bold text-foreground sm:text-2xl md:text-3xl">
            Everything you need to master Git
          </h2>
          <p className="mt-3 text-sm text-muted-foreground sm:text-base">
            A practical, interactive approach to learning version control.
          </p>
        </div>

        <div className="grid gap-4 sm:grid-cols-2 sm:gap-6 lg:grid-cols-4">
          {features.map((feature) => (
            <div
              key={feature.title}
              className="rounded-xl border border-border bg-card p-5 transition-colors hover:border-primary/30 sm:p-6"
            >
              <div className="mb-4 flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
                <feature.icon className="h-5 w-5 text-primary" />
              </div>
              <h3 className="mb-2 font-semibold text-card-foreground">
                {feature.title}
              </h3>
              <p className="text-sm leading-relaxed text-muted-foreground">
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}