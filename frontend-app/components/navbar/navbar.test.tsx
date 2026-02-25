import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { Navbar } from "./navbar";
import { mockAuth } from "@/tests/mocks/auth.mock";

describe("Navbar When Logged Out", () => {
    test("Shows The Sign In And Get Started Buttons When User Is Not Authenticated", async () => {
        mockAuth.isAuthenticated = false;

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
        mockAuth.isAuthenticated = true;
        mockAuth.user = { firstName: "John", lastName: "Doe" };

        render(<Navbar />);

        const userButton = screen.getByTitle("Open the user menu");
        expect(userButton).toBeInTheDocument();
        fireEvent.click(userButton);

        const profileName = await screen.getByText("John");
        expect(profileName).toBeInTheDocument();
    });
});