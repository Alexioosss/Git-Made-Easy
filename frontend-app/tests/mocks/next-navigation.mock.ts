export const mockPush = jest.fn();
export const mockPathname = "/";

jest.mock("next/navigation", () => ({
    useRouter: () => ({ push: mockPush }),
    usePathname: () => mockPathname
}));