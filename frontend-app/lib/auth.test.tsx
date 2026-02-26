import { createMockUser } from "@/tests/mocks/mockUser";
import { getCurrentUser, logoutUser, hasToken } from "./auth";
import { GatewayFactory } from "@/config/GatewayFactory";

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      authGateway: {
        getCurrentUser: jest.fn(),
        logout: jest.fn()
      }
    }
  }
}));


describe("Auth Utility Class", () => {
    let cookieJar: string; // A mock for the cookie storage, since it is not fully functional / persistent during tests in JSDOM

    beforeEach(() => {
        cookieJar = "";
        jest.spyOn(document, "cookie", "set").mockImplementation((cookie: string) => { cookieJar += cookie; }); // Spy on the document.cookies object and when setting cookies, add them to the above mock string
        jest.spyOn(document, "cookie", "get").mockImplementation(() => { return cookieJar; }); // Similarly for get, return the mocked cookie object
        jest.clearAllMocks();
    });


    describe("getCurrentUser", () => {
        test("Returns the current user", async () => {
            const mockUser = createMockUser();
            (GatewayFactory.instance.authGateway.getCurrentUser as jest.Mock).mockResolvedValue(mockUser);
            
            const user = await getCurrentUser();

            expect(user).toEqual(mockUser);
            expect(GatewayFactory.instance.authGateway.getCurrentUser).toHaveBeenCalled();
        });

        test("Returns null when gateway errors / throws exception", async () => {
            (GatewayFactory.instance.authGateway.getCurrentUser as jest.Mock).mockRejectedValue(new Error("Could not load current user"));

            const user = await getCurrentUser();

            expect(user).toBeNull();
        });
    });

    describe("logoutUser", () => {
        test("Calls logout and clears the browser's cookie", async () => {
            (GatewayFactory.instance.authGateway.logout as jest.Mock).mockResolvedValue(undefined);

            document.cookie= "access_token=my-secret-access-token;";

            const result = await logoutUser();

            expect(result).toBe(true);
            expect(GatewayFactory.instance.authGateway.logout).toHaveBeenCalled();
            expect(document.cookie).toContain("access_token=");
            expect(document.cookie).toContain("Max-Age=0");
        });

        test("returns false when logout throws", async () => {
            (GatewayFactory.instance.authGateway.logout as jest.Mock).mockRejectedValue(new Error("Could not perform logout"));

            const result = await logoutUser();

            expect(result).toBe(false);
        });
    });

    describe("hasToken", () => {
        test("returns true when access_token cookie exists", () => {
            document.cookie = "foo=1; access_token=abc; bar=2";
            expect(hasToken()).toBe(true);
        });

        test("returns false when access_token cookie does not exist", () => {
            document.cookie = "my=1; token=2";
            expect(hasToken()).toBe(false);
        });
    });
});