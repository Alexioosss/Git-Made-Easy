import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchDashboardGateway } from "./FetchDashboardGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockDashboardData } from "@/tests/mocks/mockDashboardData";

describe("FetchDashboardGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchDashboardGateway;

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchDashboardGateway(apiClient);
    });


    test("getDashboardData Cakks ApiRequest Correctly", async () => {
        const mockResponse = createMockDashboardData();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getDashboardData();

        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/dashboard", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });
});