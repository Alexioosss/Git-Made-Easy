import { render, screen } from "@testing-library/react";
import LessonDetailPage from "./LessonDetailPage";
import { GatewayFactory } from "@/config/GatewayFactory";
import { notFound } from "next/navigation";

jest.mock("next/navigation", () => ({
  notFound: jest.fn()
}));

jest.mock("@/components/lessons/lesson-header", () => ({
  LessonHeader: ({ lesson, progress }: any) => (
    <div data-testid="lesson-header" data-progress={progress ? "true" : "false"}>
      {lesson.title}
    </div>
  )
}));

jest.mock("@/components/lessons/task-list", () => ({
  TaskList: ({ lesson, nextLesson }: any) => (
    <div data-testid="task-list" data-next={nextLesson?.lessonId ?? "none"}>
      {lesson.title}
    </div>
  )
}));

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      lessonGateway: {
        getById: jest.fn(),
        getAll: jest.fn()
      },
      lessonProgressGateway: {
        getLessonProgress: jest.fn()
      }
    }
  }
}));


describe("LessonDetailClient", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("calls notFound() when lesson does not exist", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue(null);
        (GatewayFactory.instance.lessonGateway.getAll as jest.Mock).mockResolvedValue([]);

        await LessonDetailPage({ lessonId: "123" });

        expect(notFound).toHaveBeenCalled();
    });

    test("Renders lesson header and task list when lesson exists", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue({ lessonId: "1", title: "Lesson 1" });

        (GatewayFactory.instance.lessonGateway.getAll as jest.Mock).mockResolvedValue([ { lessonId: "1", title: "Lesson 1" }, { lessonId: "2", title: "Lesson 2" } ]);

        (GatewayFactory.instance.lessonProgressGateway.getLessonProgress as jest.Mock).mockResolvedValue({ lessonId: "1", completed: true });

        render(await LessonDetailPage({ lessonId: "1" }));

        expect(screen.getByTestId("lesson-header")).toBeInTheDocument();
        expect(screen.getByTestId("task-list")).toBeInTheDocument();
        expect(screen.getByTestId("lesson-header")).toHaveAttribute("data-progress", "true");
        expect(screen.getByTestId("task-list")).toHaveAttribute("data-next", "2");
    });

    test("Handles missing progress gracefully", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue({ lessonId: "1", title: "Lesson 1" });

        (GatewayFactory.instance.lessonGateway.getAll as jest.Mock).mockResolvedValue([ { lessonId: "1", title: "Lesson 1" } ]);

        (GatewayFactory.instance.lessonProgressGateway.getLessonProgress as jest.Mock).mockRejectedValue(new Error("No progress"));

        render(await LessonDetailPage({ lessonId: "1" }));

        expect(screen.getByTestId("lesson-header")).toHaveAttribute("data-progress", "false");
        expect(screen.getByTestId("task-list")).toHaveAttribute("data-next", "none");
    });

    test("Correctly identifies next lesson", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue({ lessonId: "2", title: "Lesson 2" });

        (GatewayFactory.instance.lessonGateway.getAll as jest.Mock).mockResolvedValue([
            { lessonId: "1", title: "Lesson 1" }, { lessonId: "2", title: "Lesson 2" }, { lessonId: "3", title: "Lesson 3" }
        ]);

        (GatewayFactory.instance.lessonProgressGateway.getLessonProgress as jest.Mock).mockResolvedValue(undefined);

        render(await LessonDetailPage({ lessonId: "2" }));

        expect(screen.getByTestId("task-list")).toHaveAttribute("data-next", "3");
    });
});