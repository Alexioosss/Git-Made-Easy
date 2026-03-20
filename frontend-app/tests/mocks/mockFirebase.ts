jest.mock("firebase/app", () => ({ initializeApp: jest.fn() }));
jest.mock("firebase/auth", () => ({
    getAuth: jest.fn(() => ({
        currentUser: { email: "mockemail@test.com" }
    })),
    signInWithEmailAndPassword: jest.fn(() => Promise.resolve({})),
    sendEmailVerification: jest.fn(() => Promise.resolve())
}));