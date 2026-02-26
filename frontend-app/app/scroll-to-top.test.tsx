import { usePathname } from "next/navigation";
import { ScrollToTop } from "./scroll-to-top";
import { render } from "@testing-library/react";

jest.mock("next/navigation", () => ({
    usePathname: jest.fn()
}));
const mockUsePathname = usePathname as jest.Mock;


describe("ScrollToTop", () => {
    beforeAll(() => { window.scrollTo = jest.fn(); });
    afterEach(() => { jest.clearAllMocks(); });

    it("Calls window.scrollTo when the route (page) changes", () => {
        mockUsePathname.mockReturnValue("/");

        const { rerender } = render(<ScrollToTop />);
        expect(window.scrollTo).toHaveBeenCalledWith({ top: 0, behavior: "smooth" });

        mockUsePathname.mockReturnValue("/lessons");

        rerender(<ScrollToTop />);
        expect(window.scrollTo).toHaveBeenCalledWith({ top: 0, behavior: "smooth" });
    });
});