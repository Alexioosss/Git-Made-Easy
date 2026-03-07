import { render, screen, waitFor, act } from "@testing-library/react";
import DashboardClient from "./DashboardClient";
import { GatewayFactory } from "@/config/GatewayFactory";

const mockPush = jest.fn();
const mockUseAuth = jest.fn();

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      dashboardGateway: {
        getDashboardData: jest.fn()
      }
    }
  }
}));

jest.mock("@/lib/auth", () => ({
  getCurrentUser: jest.fn(),
  hasToken: jest.fn()
}));

jest.mock("@/context/AuthContext", () => ({
    useAuth: () => mockUseAuth()
}));

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push: mockPush })
}));

jest.mock("@/components/dashboard/dashboard-lessons", () => ({
  DashboardLessons: () => <div data-testid="dashboard-lessons" />
}));

jest.mock("@/components/dashboard/dashboard-stats", () => ({
  DashboardStats: () => <div data-testid="dashboard-stats" />
}));

jest.mock("@/components/dashboard/recent-activity", () => ({
  RecentActivity: ({ activities }: any) => (
    <div data-testid="recent-activity">{activities.length} activities</div>
  )
}));

const mockGetDashboard = GatewayFactory.instance.dashboardGateway
  .getDashboardData as jest.Mock;

const { getCurrentUser } = jest.requireMock("@/lib/auth");


describe("DashboardClient", () => {
    beforeEach(() => {
        jest.clearAllMocks();
        mockUseAuth.mockReturnValue({ isAuthenticated: true });
    });

    test("Redirects to /login when no user is found", async () => {
        mockUseAuth.mockReturnValueOnce({ isAuthenticated: false });

        await act(async () => {
            render(<DashboardClient />);
        });

        await waitFor(() => {
            expect(mockPush).toHaveBeenCalledWith("/login");
        });
    });

    test("Shows loading spinner initially", async () => {
        getCurrentUser.mockResolvedValueOnce({ id: "123" });
        mockGetDashboard.mockImplementation(() => new Promise(() => {}));

        let rendered: ReturnType<typeof render>;
        await act(async () => {
            rendered = render(<DashboardClient />);
        });

        const spinner = rendered!.container.querySelector(".animate-spin");
        expect(spinner).not.toBeNull();
    });

    test("Loads dashboard data and renders stats + lessons + activity", async () => {
        getCurrentUser.mockResolvedValueOnce({ id: "123", firstName: "John" });
        mockGetDashboard.mockResolvedValueOnce({
            firstName: "John",
            lessons: [{ lessonId: "1", title: "Lesson 1" }],
            tasksProgress: [
                {
                taskId: "t1",
                taskTitle: "Task 1",
                lessonId: "1",
                attempts: 2,
                status: "COMPLETED",
                completedAt: "2024-01-01"
                }
            ]
        });

        await act(async () => {
            render(<DashboardClient />);
        });

        expect(await screen.findByText(/welcome back, john/i)).toBeInTheDocument();
        expect(screen.getByTestId("dashboard-stats")).toBeInTheDocument();
        expect(screen.getByTestId("dashboard-lessons")).toBeInTheDocument();
        expect(screen.getByTestId("recent-activity")).toHaveTextContent("1 activities");
    });

    test("Shows error message when dashboard fetch fails", async () => {
        getCurrentUser.mockResolvedValueOnce({ id: "123" });
        mockGetDashboard.mockRejectedValueOnce(new Error("Failed to load"));

        await act(async () => {
            render(<DashboardClient />);
        });
        
        expect(await screen.findByText(/failed to load/i)).toBeInTheDocument();
    });

    test("Transforms activities correctly", async () => {
        getCurrentUser.mockResolvedValueOnce({ id: "123" });
        mockGetDashboard.mockResolvedValueOnce({
            firstName: "Jane",
            lessons: [
                { lessonId: "1", title: "Lesson A" },
                { lessonId: "2", title: "Lesson B" }
            ],
            tasksProgress: [
                {
                taskId: "t1",
                taskTitle: "Task 1",
                lessonId: "1",
                attempts: 1,
                status: "IN_PROGRESS",
                startedAt: "2024-01-02"
                },
                {
                taskId: "t2",
                taskTitle: "Task 2",
                lessonId: "2",
                attempts: 1,
                status: "COMPLETED",
                completedAt: "2024-01-01"
                },
                {
                taskId: "t3",
                taskTitle: "Task 3",
                lessonId: "1",
                attempts: 1,
                status: "NOT_STARTED"
                }
            ]
        });

        await act(async () => {
            render(<DashboardClient />);
        });

        const activityComponent = await screen.findByTestId("recent-activity");
        expect(activityComponent).toHaveTextContent("2 activities");
    });
});