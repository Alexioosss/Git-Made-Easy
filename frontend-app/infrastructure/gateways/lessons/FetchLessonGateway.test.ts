import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchLessonGateway } from "./FetchLessonGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockTask } from "@/tests/mocks/mockTask";
import { createMockLesson } from "@/tests/mocks/mockLesson";

describe("FetchLessonGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchLessonGateway

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchLessonGateway(apiClient);
    });

    
    test("createLesson Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockLesson();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.createLesson("Title", "Description", "EASY");
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons", HttpMethods.POST,
            {
                title: "Title", description: "Description", difficulty: "EASY"
            }
        );
        expect(response).toEqual(mockResponse);
    });

    test("getById Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockLesson();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getById("1");
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons/1", HttpMethods.GET, undefined, { cache: "force-cache" });
        expect(response).toEqual(mockResponse);
    });
    
    test("getAll Calls ApiRequest Correctly", async () => {
        const mockResponse = [createMockLesson(), createMockLesson({ id: "2" })];
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getAllLessons();
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons", HttpMethods.GET, undefined, { cache: "force-cache" });
        expect(response).toEqual(mockResponse);
    });
    
    test("getTasksForLesson Calls ApiRequest Correctly", async () => {
        const lessonId = "1";
        const mockTasks = [ createMockTask({ lessonId, taskId: "Task1" }), createMockTask({ lessonId, taskId: "Task2" }) ];
        apiClient.apiRequest.mockResolvedValue(mockTasks);
        const response = await gateway.getTasksForLesson(lessonId);
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            `/lessons/${lessonId}/tasks`, HttpMethods.GET, undefined, { cache: "force-cache" });
        expect(response).toEqual(mockTasks);
    });
});