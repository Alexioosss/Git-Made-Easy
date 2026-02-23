module.exports = {
    testEnvironment: "jsdom",
    collectCoverage: true,
    coverageDirectory: "coverage",
    coverageReporters: ["json-summary", "text", "lcov"],
    setupFilesAfterEnv: ["@testing-library/jest-dom"]
};