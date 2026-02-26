import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchAuthGateway } from "./FetchAuthGateway";
import { HttpMethods } from "../HttpMethods";

describe("FetchAuthGateway", () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchAuthGateway

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchAuthGateway(apiClient);
    });
    

    test("login Calls ApiRequest Correctly", async () => {
        const mockResponse = { accessToken: "my-new-secret-auth-token" };
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.login("myemail1@gmail.com", "MyPassword123'");
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/auth/login", HttpMethods.POST,
            {
                email: "myemail1@gmail.com",
                password: "MyPassword123'"
            }
        );
        expect(response).toEqual(mockResponse);
    });

    test("logout Calls ApiRequest Correctly", async () => {
        await gateway.logout();
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/auth/logout", HttpMethods.POST);
    });

    test("refresh Calls ApiRequest Correctly", async () => {
        const mockResponse = { accessToken: "my-refreshed-auth-token" };
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.refresh();
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/auth/refresh", HttpMethods.POST);
        expect(response).toEqual(mockResponse);
    });

    test("getCurrentUser Calls ApiRequest Correctly", async () => {
        const mockResponse = { id: "1", firstName: "John", lastName: "Doe", emailAddress: "myemail1@gmail.com" };
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getCurrentUser();
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/auth/me", HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });
});