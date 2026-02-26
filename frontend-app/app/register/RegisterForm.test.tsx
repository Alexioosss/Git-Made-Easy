import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import RegisterForm from "./RegisterForm";
import { GatewayFactory } from "@/config/GatewayFactory";

const mockRegister = GatewayFactory.instance.userGateway.register as jest.Mock;
const mockPush = jest.fn();

jest.mock("@/config/GatewayFactory", () => ({
  GatewayFactory: {
    instance: {
      userGateway: {
        register: jest.fn()
      }
    }
  }
}));

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push: jest.fn() })
}));

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push: mockPush })
}));


describe("RegisterForm", () => {
    beforeEach(() => {
        jest.useFakeTimers();
        jest.clearAllMocks();
    });

    test("Renders all input fields", () => {
        render(<RegisterForm />);

        expect(screen.getByLabelText(/first name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/last name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/email address/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    });

    test("Calls register with correct values", async () => {
        mockRegister.mockResolvedValueOnce({});

        render(<RegisterForm />);

        fireEvent.change(screen.getByLabelText(/first name/i), { target: { value: "John" } });
        fireEvent.change(screen.getByLabelText(/last name/i), { target: { value: "Doe" } });
        fireEvent.change(screen.getByLabelText(/email address/i), { target: { value: "john@example.com" } });
        fireEvent.change(screen.getByLabelText(/password/i), { target: { value: "password123" } });
        fireEvent.submit(screen.getByRole("button", { name: /create account/i }));

        await waitFor(() => {
        expect(mockRegister).toHaveBeenCalledWith(
            "John", "Doe", "john@example.com", "password123");
        });
    });

    test("Shows error message when registration fails", async () => {
        mockRegister.mockRejectedValueOnce({ message: "Email already exists" });

        render(<RegisterForm />);

        fireEvent.submit(screen.getByRole("button", { name: /create account/i }));

        expect(await screen.findByText(/email already exists/i)).toBeInTheDocument();
    });

    test("Shows fallback error when no fail message is provided", async () => {
        mockRegister.mockRejectedValueOnce({});

        render(<RegisterForm />);

        fireEvent.submit(screen.getByRole("button", { name: /create account/i }));

        expect(await screen.findByText(/something went wrong/i)).toBeInTheDocument();
    });

    test("Shows success screen after successful registration", async () => {
        mockRegister.mockResolvedValueOnce({});

        render(<RegisterForm />);

        fireEvent.submit(screen.getByRole("button", { name: /create account/i }));

        expect(await screen.findByText(/account created successfully/i)).toBeInTheDocument();
    });

    test("Redirects to /login after successful registration", async () => {
        mockRegister.mockResolvedValueOnce({});

        render(<RegisterForm />);

        fireEvent.submit(screen.getByRole("button", { name: /create account/i }));

        await screen.findByText(/account created successfully/i);
        jest.runAllTimers();

        expect(mockPush).toHaveBeenCalledWith("/login");
    });
});