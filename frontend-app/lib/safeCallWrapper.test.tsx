import { safeCallWrapper } from "./safeCallWrapper";

describe("safeCallWrapper", () => {
    it("Should return a success object when the promise resolves / is successful", async () => {
        const mockData = { id: 1, name: "Test" };
        const result = await safeCallWrapper(Promise.resolve(mockData));

        expect(result).toEqual({
            ok: true,
            data: mockData,
            error: null
        });
    });

    it("Should return an error object when the promise rejects / fails with an error message", async () => {
        const errorMsg = "Something went wrong";
        const mockPromise = Promise.reject(new Error(errorMsg));
        const result = await safeCallWrapper(mockPromise);

        expect(result).toEqual({
            ok: false,
            data: null,
            error: errorMsg,
            status: 0,
            code: null
        });
    });

    it("Should successfully handle errors with custom status, code, and message", async () => {
        const errorObj = {
            message: "Payment required",
            status: 402,
            code: "PAYMENT_REQUIRED"
        };
        const mockPromise = Promise.reject(errorObj);
        const result = await safeCallWrapper(mockPromise);

        expect(result).toEqual({
            ok: false,
            data: null,
            error: "Payment required",
            status: 402,
            code: "PAYMENT_REQUIRED"
        });
    });
});