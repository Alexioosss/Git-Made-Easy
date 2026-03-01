# 🎯 Git Made Easy - Backend API

This Spring Boot backend powers an interactive platform designed to teach Git through lessons, tasks, hints, and feedback. It offers secure user authentication, lesson management, task tracking, and progress monitoring, seamlessly integrated with the frontend learning interface.

---

## 🚀 Overview

Built with Java and Spring Boot, this backend leverages **clean architecture principles** to ensure modularity, scalability, and maintainability. Its design emphasizes core software engineering concepts, including:

- **Dependency Inversion Principle (DIP):** Decoupling high-level modules from low-level data access implementations through well-defined interfaces.
- **DRY (Don't Repeat Yourself):** Reusing components such as mappers, repositories, and configurations to avoid redundancy.
- **Design Patterns:**
    - **Strategy Pattern:** Facilitates interchangeable data store strategies (Firebase, JPA) via configurable gateways.
    - **Adapter Pattern:** Encapsulates external data schemas and APIs, translating them into internal domain models and interfaces.
    - **Bridge Pattern:** Decouples abstraction (gateway interfaces) from implementations, enabling flexible data source switching.
    - **Facade Pattern:** Provides simplified interfaces (e.g., `LessonProgressFacade`) to coordinate complex subsystem interactions, hiding implementation details.
    - **Singleton Pattern:** Ensures shared, single instances of stateless components like mappers, configuration classes, and repositories for efficient resource management.

Thanks to its **modular and layered architecture**, the platform can easily adapt to different data store implementations and deployment scenarios, supporting extensibility and future growth.

---

## 🔧 Technologies

- Java 21
- Spring Boot 3.5.10
- Spring Security & JJWT 0.11.5
- Spring Data JPA
- Firebase Admin SDK 9.2.0
- Google Cloud Firestore
- SLF4J Logging
- Spring Mail
- JUnit 5
- Code Coverage with JaCoCo 0.8.12

---

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8.0 or higher
- Firebase credentials and project setup
- Configure environment variables or `.properties` files