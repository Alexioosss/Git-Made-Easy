import { createMockApiClient } from "@/tests/mocks/mockApiClient";
import { FetchUserGateway } from "./FetchUserGateway";
import { HttpMethods } from "../HttpMethods";
import { createMockUser } from "@/tests/mocks/mockUser";

describe("FetchUserGateway" , () => {
    let apiClient: ReturnType<typeof createMockApiClient>;
    let gateway: FetchUserGateway

    beforeEach(() => {
        apiClient = createMockApiClient();
        gateway = new FetchUserGateway(apiClient);
    });
    

    test("register Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockUser();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.register("John", "Doe", "myemail1@gmail.com", "MyPassword123'");
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/users", HttpMethods.POST,
            {
                firstName: "John",
                lastName: "Doe",
                emailAddress: "myemail1@gmail.com",
                password: "MyPassword123'",
            }
        );
        expect(response).toEqual(mockResponse);
    });

    test("getById Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockUser();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const userId = "1";
        const response = await gateway.getById(userId);
        expect(apiClient.apiRequest).toHaveBeenCalledWith(`/users/${userId}`, HttpMethods.GET);
        expect(response).toEqual(mockResponse);
    });

    test("getByEmailAddress Calls ApiRequest Correctly", async () => {
        const mockResponse = createMockUser();
        apiClient.apiRequest.mockResolvedValue(mockResponse);
        const response = await gateway.getByEmailAddress("myemail1@gmail.com");
        expect(apiClient.apiRequest).toHaveBeenCalledWith(
            "/users", HttpMethods.GET, { emailAddress: "myemail1@gmail.com" }
        );
    });
});