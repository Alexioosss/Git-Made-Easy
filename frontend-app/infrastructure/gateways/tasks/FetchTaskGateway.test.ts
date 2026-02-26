import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchTaskGateway } from "./FetchTaskGateway";
import { DifficultyLevels } from "@/types/difficultyLevels";
import { HttpMethods } from "../HttpMethods";
import { createMockTask } from "@/tests/mocks/mockTask";

describe("FetchTaskGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchTaskGateway

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchTaskGateway(apiClient);
    });
    

    test("getById Calls ApiRequest Correctly", async () => {
        const lessonId = "1", taskId = "1";
        const mockResponse = createMockTask({lessonId, taskId});
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getById(lessonId, taskId);
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            `/lessons/${lessonId}/tasks/${taskId}`, HttpMethods.GET, undefined, { "cache": "force-cache" });
        expect(response).toEqual(mockResponse);
    });
});