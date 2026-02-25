module.exports = {
    testEnvironment: "jsdom",
    collectCoverage: true,
    coverageDirectory: "coverage",
    coverageReporters: ["json-summary", "text", "lcov"],
    setupFilesAfterEnv: ["<rootDir>/tests/setup/jest.setup.js"],
    transform: {
        "^.+\\.(js|jsx|ts|tsx)$": "babel-jest"
    },
    moduleNameMapper: {
        "^@/(.*)$": "<rootDir>/$1",
        "\\.(css|less|scss|sass)$": "identity-obj-proxy"
    }
};