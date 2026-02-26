import { fireEvent, render, screen, waitFor, within } from "@testing-library/react";
import { Navbar } from "./navbar";
import { mockAuthentication } from "@/tests/mocks/mockAuthentication";
import { usePathname, useRouter } from "next/navigation";

jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
    usePathname: jest.fn()
}));

const mockPush = jest.fn();

beforeEach(() => {
    jest.clearAllMocks();
    (useRouter as jest.Mock).mockReturnValue({ push: mockPush });
    (usePathname as jest.Mock).mockReturnValue("/");
});

describe("Navbar When Logged Out", () => {
    test("Shows The Sign In And Get Started Buttons When User Is Not Authenticated", async () => {
        mockAuthentication.isAuthenticated = false;

        render(<Navbar />);
        
        const signInButtons = await screen.getAllByText("Sign In");
        const getStartedButtons = await screen.getAllByText("Get Started");

        expect(signInButtons.length).toBeGreaterThanOrEqual(1);
        expect(getStartedButtons.length).toBeGreaterThanOrEqual(1);
        expect(screen.queryByText("Sign Out")).not.toBeInTheDocument();
    });
});

describe("Navbar When Logged In", () => {
    test("Shows The User's Name In The Navbar When User Is Authenticated", async () => {
        mockAuthentication.isAuthenticated = true;
        mockAuthentication.user = { firstName: "John", lastName: "Doe" };

        render(<Navbar />);

        const userButton = screen.getByTitle("Open the user menu");
        expect(userButton).toBeInTheDocument();
        fireEvent.click(userButton);

        const profileName = await screen.getByText("John");
        expect(profileName).toBeInTheDocument();
    });
});

describe("Navbar Mobile Menu", () => {
    test("Hamburger button toggles mobile menu open and closed", () => {
        mockAuthentication.isAuthenticated = false;

        render(<Navbar />);

        const toggleButton = screen.getByLabelText("Open menu");
        fireEvent.click(toggleButton);

        const mobileMenu = document.querySelector("div.md\\:hidden") as HTMLElement;

        expect(mobileMenu).toHaveClass("max-h-96");
        expect(mobileMenu).toHaveClass("opacity-100");
        expect(mobileMenu).toHaveClass("pointer-events-auto");

        fireEvent.click(screen.getByLabelText("Close menu"));

        expect(mobileMenu).toHaveClass("max-h-0");
        expect(mobileMenu).toHaveClass("opacity-0");
        expect(mobileMenu).toHaveClass("pointer-events-none");
    });
    
    test("Clicking a mobile link closes the menu", () => {
        mockAuthentication.isAuthenticated = false;

        render(<Navbar />);

        fireEvent.click(screen.getByLabelText("Open menu"));

        const mobileMenu = document.querySelector("div.md\\:hidden") as HTMLElement;
        const lessonsLink = within(mobileMenu).getByText("Lessons");
        fireEvent.click(lessonsLink);

        expect(mobileMenu).toHaveClass("pointer-events-none");
    });
});

describe("Navbar Active Link Highlighting", () => {
    test("Highlights Lessons when pathname starts with /lessons", () => {
        (usePathname as jest.Mock).mockReturnValue("/lessons");

        render(<Navbar />);

        const header = screen.getByRole("banner");
        const desktopNav = header.querySelector("nav.md\\:flex") as HTMLElement;
        const lessonsLink = within(desktopNav).getByText("Lessons");
        expect(lessonsLink).toHaveClass("font-bold");
    });
    
    test("Highlights Dashboard when pathname starts with /dashboard", () => {
        mockAuthentication.isAuthenticated = true;
        mockAuthentication.user = { firstName: "John", lastName: "Doe" };
        (usePathname as jest.Mock).mockReturnValue("/dashboard");

        render(<Navbar />);

        const dashboardLinks = screen.getAllByText("Dashboard");
        const active = dashboardLinks.find(link => link.classList.contains("font-bold"));
        expect(active).toBeTruthy();
    });
});

describe("Navbar Theme Toggle", () => {
    test("Theme toggle renders", () => {
        render(<Navbar />);
        expect( screen.getByRole("button", { name: /switch to/i }) ).toBeInTheDocument();
    });
});

describe("Navbar Responsive Rendering", () => {
    test("Mobile menu button is visible", () => {
        render(<Navbar />);
        expect(screen.getByLabelText("Open menu")).toBeInTheDocument();
    });
    
    test("Desktop nav renders Lessons link", () => {
        render(<Navbar />);
        const header = screen.getByRole("banner");
        const desktopNav = header.querySelector("nav.md\\:flex") as HTMLElement;
        expect(desktopNav).toBeInTheDocument();
        expect(within(desktopNav).getByText("Lessons")).toBeInTheDocument();
    });
});