export function createMockLesson(overrides = {}) {
    return {
        id: "1",
        title: "Lesson Title",
        description: "Lesson Description",
        difficulty: "EASY",
        ...overrides
    };
}