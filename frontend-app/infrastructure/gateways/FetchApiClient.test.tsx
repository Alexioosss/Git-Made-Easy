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

    test("retries after 401 and refresh succeeds", async () => {
        mockFetch.mockResolvedValueOnce({
            ok: false,
            status: 401,
            headers: { get: () => "application/json" },
            json: async () => ({})
        }).mockResolvedValueOnce({ ok: true })
        .mockResolvedValueOnce({ ok: true, headers: { get: () => "application/json" }, json: async () => ({ retried: true }) });
        
        const result = await client.apiRequest("/test", HttpMethods.GET);
        expect(result).toEqual({ retried: true });
    });
    
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