import { render, screen, waitFor } from "@testing-library/react";
import SingleLessonPage from "./page";
import { GatewayFactory } from "@/config/GatewayFactory";
import { createMockLesson } from "@/tests/mocks/mockLesson";
import { createMockLessonProgress } from "@/tests/mocks/mockLessonProgress";
import { act } from "react";

jest.mock("next/navigation", () => ({
  notFound: jest.fn()
}));

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      lessonGateway: {
        getById: jest.fn(),
        getAllLessons: jest.fn()
      },
      lessonProgressGateway: {
        getLessonProgress: jest.fn()
      }
    }
  }
}));

describe("LessonDetailPage", () => {
    beforeEach(() => jest.clearAllMocks());

    test("Calls notFound when lesson missing", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue(null);

        await act(async () => { render(await SingleLessonPage({ params: Promise.resolve({ lessonId: "1" }) })); });
        
        await waitFor(() => { expect(screen.getByText(/Could not load lesson/i)).toBeInTheDocument(); });
    });

    test("Passes correct props to LessonDetail", async () => {
        (GatewayFactory.instance.lessonGateway.getById as jest.Mock).mockResolvedValue(createMockLesson({ lessonId: "1", title: "Lesson 1" }));
        (GatewayFactory.instance.lessonGateway.getAllLessons as jest.Mock).mockResolvedValue([ createMockLesson(), createMockLesson({ lessonId: "2" }) ]);
        (GatewayFactory.instance.lessonProgressGateway.getLessonProgress as jest.Mock).mockResolvedValue(createMockLessonProgress());

        await act(async () => { render(await SingleLessonPage({ params: Promise.resolve({ lessonId: "1" }) })); });

        expect(screen.getByText("Lesson 1")).toBeInTheDocument();
        expect(screen.getByText("100%")).toBeInTheDocument();
        expect(screen.getByText("01")).toBeInTheDocument();
    });
});