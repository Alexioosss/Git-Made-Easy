import type { AuthContextType } from "@/context/AuthContext";

export const mockAuthentication: AuthContextType = {
    user: null,
    isAuthenticated: false,
    refreshUser: jest.fn(),
    logout: jest.fn()
};

jest.mock("@/context/AuthContext", () => ({ useAuth: () => mockAuthentication }));