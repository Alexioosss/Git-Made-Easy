import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { Navbar } from "./navbar";
import { mockAuthentication } from "@/tests/mocks/mockAuthentication";

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