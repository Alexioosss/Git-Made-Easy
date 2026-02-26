import { act, fireEvent, render } from "@testing-library/react";
import ThemeToggle from "./theme-toggle";
import { useTheme } from "next-themes";

jest.mock("next-themes", () => ({
  useTheme: jest.fn(),
}));

const mockUseTheme = useTheme as jest.Mock;


describe("ThemeToggle", () => {
    beforeEach(() => {
        jest.useFakeTimers();
        jest.clearAllMocks();
    });

    afterEach(() => {
        jest.useRealTimers();
    });


    it("Renders when mounted", () => {
        mockUseTheme.mockReturnValue({
            theme: "light",
            setTheme: jest.fn(),
            resolvedTheme: "light",
        });
        const { getByRole } = render(<ThemeToggle />);
        const button = getByRole("button");
        expect(button).toBeInTheDocument();
    });

    it("Calls setTheme with the correct theme when clicked", async () => {
        const setThemeMock = jest.fn();

        mockUseTheme.mockReturnValue({
            theme: "light",
            setTheme: setThemeMock,
            resolvedTheme: "light",
        });

        const { getByRole, rerender } = render(<ThemeToggle />);
        const button = getByRole("button");

        fireEvent.click(button);
        await act(async () => { jest.advanceTimersByTime(150); });
        expect(setThemeMock).toHaveBeenCalledWith("dark");

        mockUseTheme.mockReturnValue({
            theme: "dark",
            setTheme: setThemeMock,
            resolvedTheme: "dark",
        });
        rerender(<ThemeToggle />);

        fireEvent.click(button);
        await act(async () => { jest.advanceTimersByTime(150); });
        expect(setThemeMock).toHaveBeenCalledWith("system");

        mockUseTheme.mockReturnValue({
            theme: "system",
            setTheme: setThemeMock,
            resolvedTheme: "light",
        });
        rerender(<ThemeToggle />);

        fireEvent.click(button);
        await act(async () => { jest.advanceTimersByTime(150); });
        expect(setThemeMock).toHaveBeenCalledWith("light");
    });
});