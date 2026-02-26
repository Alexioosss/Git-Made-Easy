import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchLessonProgressGateway } from "./FetchLessonProgressGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockLessonProgress } from "@/tests/mocks/mockLessonProgress";

describe("FetchLessonProgressGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchLessonProgressGateway;

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchLessonProgressGateway(apiClient);
    });
    

    test("getLessonProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockLessonProgress({ lessonId: "1" });
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getLessonProgress("1");

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons/1/progress", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });

    test("getAllLessonsProgress Calls ApiRequest Correctly", async () => {
        const mockResponse = [ createMockLessonProgress({ lessonProgressId: "lp-1" }), createMockLessonProgress({ lessonProgressId: "lp-2" }) ];
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getAllLessonsProgress();

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/lessons/progress", HttpMethods.GET );
        expect(response).toEqual(mockResponse);
    });
});