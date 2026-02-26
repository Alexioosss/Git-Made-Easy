import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import LoginForm from "./LoginForm";
import { GatewayFactory } from "@/config/GatewayFactory";

const mockPush = jest.fn();

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      authGateway: {
        login: jest.fn()
      }
    }
  }
}));

jest.mock("@/context/AuthContext", () => ({
  useAuth: () => ({ refreshUser: jest.fn() })
}));

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push: mockPush })
}));

const mockLogin = GatewayFactory.instance.authGateway.login as jest.Mock;


describe("LoginForm", () => {
    beforeEach(() => {
      jest.useFakeTimers();
      jest.clearAllMocks();
    });
    
    test("Renders email and password fields", () => {
      render(<LoginForm />);
      expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    });

    test("Calls login with valid email and password values", async () => {
      mockLogin.mockResolvedValueOnce({});
      render(<LoginForm />);

      fireEvent.change(screen.getByLabelText(/email/i), {
        target: { value: "test@example.com" }
      });
      fireEvent.change(screen.getByLabelText(/password/i), {
        target: { value: "password123" }
      });

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      await waitFor(() => {
        expect(mockLogin).toHaveBeenCalledWith("test@example.com", "password123");
      });
    });

    test("Shows error message on invalid credentials", async () => {
      mockLogin.mockRejectedValueOnce({
        code: "INVALID_CREDENTIALS",
        message: "Invalid email or password"
      });

      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      expect(await screen.findByText(/invalid email or password/i)).toBeInTheDocument();
    });

    test("Shows error message on EMAIL_NOT_VERIFIED", async () => {
      mockLogin.mockRejectedValueOnce({
        code: "EMAIL_NOT_VERIFIED",
        message: "Email not verified"
      });

      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      expect(await screen.findByText(/verify your email address via the verification link/i)).toBeInTheDocument();
    });

    test("Shows error message on AUTH_REQUIRED", async () => {
      mockLogin.mockRejectedValueOnce({
        code: "AUTH_REQUIRED"
      });

      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      expect(await screen.findByText(/your session has expired/i)).toBeInTheDocument();
    });

    test("Shows fallback error for unknown error", async () => {
      mockLogin.mockRejectedValueOnce({
        message: "Something unexpected happened"
      });

      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      expect(await screen.findByText(/something unexpected happened/i)).toBeInTheDocument();
    });

    test("Shows success screen after successful login", async () => {
      mockLogin.mockResolvedValueOnce({});
      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      expect(await screen.findByText(/logged in successfully/i)).toBeInTheDocument();
    });

    test("Redirects to /dashboard after successful login", async () => {
      mockLogin.mockResolvedValueOnce({});

      render(<LoginForm />);

      fireEvent.submit(screen.getByRole("button", { name: /sign in/i }));

      await screen.findByText(/logged in successfully/i);
      jest.runAllTimers();

      expect(mockPush).toHaveBeenCalledWith("/dashboard");
    });
});