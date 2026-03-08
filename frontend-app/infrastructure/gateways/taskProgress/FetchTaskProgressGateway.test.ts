import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchTaskProgressGateway } from "./FetchTaskProgressGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockTaskProgress } from "@/tests/mocks/mockTaskProgress";
import { LocalTaskProgress } from "@/types/localProgressData";

describe("FetchTaskProgressGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchTaskProgressGateway;

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchTaskProgressGateway(apiClient);
    });


    test("recordTaskAttempt Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockTaskProgress();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.recordTaskAttempt("1", "1", "my answer");

        expect(apiClient.apiRequest).toHaveBeenCalledWith("/lessons/1/tasks/1/progress", HttpMethods.POST, { input: "my answer" });
        expect(response).toEqual(mockResponse);
    });

    test("getTaskProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockTaskProgress({ attempts: 3 });
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getTaskProgress("1", "1");

        expect(apiClient.apiRequest).toHaveBeenCalledWith("/lessons/1/tasks/1/progress", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });

    test("getAllTaskProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = [ createMockTaskProgress({ taskId: "1" }), createMockTaskProgress({ taskId: "2" }) ];
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getAllTaskProgress();

        expect(apiClient.apiRequest).toHaveBeenCalledWith("/tasks/progress", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });

    test("syncLocalProgress Calls ApiRequest Correctly", async () => {
        const progress: LocalTaskProgress[] = [
            { taskId: "1", status: "COMPLETED", attempts: 2, lastInput: "git", lastError: "", startedAt: "2026-01-01T00:00:00Z" },
            { taskId: "2", status: "IN_PROGRESS", attempts: 1, lastInput: "git", lastError: "", startedAt: "2026-01-01T00:00:00Z" }
        ];

        const mockResponse = createMockTaskProgress();
        apiClient.apiRequest.mockResolvedValue(mockResponse);

        const response = await gateway.syncLocalProgress("1", progress);

        expect(apiClient.apiRequest).toHaveBeenCalledWith("/lessons/1/tasks/progress/sync", HttpMethods.POST, progress);
        expect(response).toEqual(mockResponse);
    });
});