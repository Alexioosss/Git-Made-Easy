import { FetchApiClient } from "./FetchApiClient";
import { HttpMethods } from "./HttpMethods";

const mockFetch = jest.fn();
global.fetch = mockFetch as any;

describe("FetchApiClient", () => {
    let client: FetchApiClient;

    beforeEach(() => {
        jest.clearAllMocks();
        client = new FetchApiClient("http://test");
    });
    
    
    test("Returns JSON on successful API call / request", async () => {
        mockFetch.mockResolvedValue({
            ok: true,
            headers: { get: () => "application/json" },
            json: async () => ({ ok: true })
        });
        
        const result = await client.apiRequest("/test", HttpMethods.GET);
        expect(result).toEqual({ ok: true });
    });

    /**
     * Test to focus on the logic applied to handle expired tokens, and attempting a refresh of the token to confirm the validity of the token for subsequent requests
     */
    test("Retries the API call / request after 401 and after refresh it succeeds", async () => {
        mockFetch.mockResolvedValueOnce({
            ok: false,
            status: 401,
            headers: { get: () => "application/json" },
            json: async () => ({})
        }).mockResolvedValueOnce({ ok: true }) // This mocks the refresh being performed and the user being authenticated for the next request
        .mockResolvedValueOnce({ ok: true, headers: { get: () => "application/json" }, json: async () => ({ retried: true }) });
        
        const result = await client.apiRequest("/test", HttpMethods.GET);
        expect(result).toEqual({ retried: true });
    });
    
    /**
     * Users are not required to be authenticated for login, logout and refreshToken requests, since the missing token in the request will dictate the response
     * When logging out, if a token is present, it will be invalidated.
     * When refreshing a token, if the token is valid (not expired and linked to a user), it can be refreshed
     * Otherwise, no refresh logic happens, 401 is returned, thus it can be treated as an invalid token from the frontend, potentially leading to the user logging in again to acquire a new token
     */
    test("Does not refresh for /auth/ paths", async () => {
        mockFetch.mockResolvedValue({
            ok: false,
            status: 401,
            headers: { get: () => "application/json" },
            json: async () => ({})
        });

        await expect(client.apiRequest("/auth/login", HttpMethods.POST)).rejects.toHaveProperty("status", 401);
        expect(mockFetch).toHaveBeenCalledTimes(1);
    });
});