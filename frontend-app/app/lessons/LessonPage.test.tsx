import { act, render, screen } from "@testing-library/react";
import LessonsPage from "./LessonPage";
import { GatewayFactory } from "@/config/GatewayFactory";
import { hasToken, getCurrentUser } from "@/lib/auth";

jest.mock("@/lib/auth", () => ({
  hasToken: jest.fn(),
  getCurrentUser: jest.fn()
}));

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      lessonGateway: {
        getAllLessons: jest.fn(),
        getTasksForLesson: jest.fn()
      },
      lessonProgressGateway: {
        getAllLessonsProgress: jest.fn()
      }
    }
  }
}));

jest.mock("@/components/lessons/lesson-card", () => ({
  LessonCard: ({ lesson, progress }: any) => (
    <div data-testid="lesson-card" data-progress={progress ? "true" : "false"}>{lesson.title}</div>
  )
}));


describe("LessonPage", () => {
  test("Renders lessons without progress when not authenticated", async () => {
    (hasToken as jest.Mock).mockReturnValue(false);

    (GatewayFactory.instance.lessonGateway.getAllLessons as jest.Mock).mockResolvedValue([{ lessonId: "1", title: "Lesson 1" }]);
    (GatewayFactory.instance.lessonGateway.getTasksForLesson as jest.Mock).mockResolvedValue([{ taskId: "t1" }]);

    await act(async () => { render(<LessonsPage />); });

    const lessonCard = await screen.findByTestId("lesson-card");
    expect(lessonCard).toBeInTheDocument();
    expect(lessonCard).toHaveAttribute("data-progress", "false");
    expect(GatewayFactory.instance.lessonProgressGateway.getAllLessonsProgress).not.toHaveBeenCalled();
  });

  test("Renders lessons with progress when authenticated", async () => {
    (hasToken as jest.Mock).mockReturnValue(true);

    (GatewayFactory.instance.lessonGateway.getAllLessons as jest.Mock).mockResolvedValue([{ lessonId: "1", title: "Lesson 1" }]);
    (GatewayFactory.instance.lessonGateway.getTasksForLesson as jest.Mock).mockResolvedValue([{ taskId: "t1" }]);
    (GatewayFactory.instance.lessonProgressGateway.getAllLessonsProgress as jest.Mock).mockResolvedValue([{ lessonId: "1", completed: true }]);

    (getCurrentUser as jest.Mock).mockResolvedValue({ id: "user1" });

    await act(async () => { render(<LessonsPage />); });

    expect(await screen.findByTestId("lesson-card")).toBeInTheDocument();
  });


  test("Attaches tasks to lessons", async () => {
      (hasToken as jest.Mock).mockReturnValue(false);

      (GatewayFactory.instance.lessonGateway.getAllLessons as jest.Mock).mockResolvedValue([{ lessonId: "1", title: "Lesson 1" }]);
      (GatewayFactory.instance.lessonGateway.getTasksForLesson as jest.Mock).mockResolvedValue([{ taskId: "t1" }, { taskId: "t2" }]);

      await act(async () => { render(<LessonsPage />); });

      expect(await screen.findByText("Lesson 1")).toBeInTheDocument();
  });
});