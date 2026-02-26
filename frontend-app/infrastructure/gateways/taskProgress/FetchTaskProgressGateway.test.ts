import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchTaskProgressGateway } from "./FetchTaskProgressGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockTaskProgress } from "@/tests/mocks/mockTaskProgress";

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

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons/1/tasks/1/progress", HttpMethods.POST);
        expect(response).toEqual(mockResponse);
    });

    test("getTaskProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockTaskProgress({ attempts: 3 });
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getTaskProgress("1", "1");

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons/1/tasks/1/progress", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });

    test("getAllLessonProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = [ createMockTaskProgress({ taskId: "1" }), createMockTaskProgress({ taskId: "2" }) ];
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getAllLessonProgress();

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/progress", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });
});