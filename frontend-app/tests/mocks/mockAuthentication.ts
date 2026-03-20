import type { AuthContextType } from "@/context/AuthContext";

export const mockAuthentication: AuthContextType = {
    user: null,
    isAuthenticated: false,
    isLoadingUser: false,
    isServerAvailable: false,
    refreshUser: jest.fn(),
    logout: jest.fn()
};

jest.mock("@/context/AuthContext", () => ({ useAuth: () => mockAuthentication }));