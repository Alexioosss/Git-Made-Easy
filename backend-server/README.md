# Git Made Easy - Backend API

This Spring Boot backend powers an interactive platform designed to teach Git through lessons and interactive elements, i.e. tasks, hints, and feedback. It offers secure user authentication, lesson management, task tracking, and progress monitoring.

---

## Overview

Built with Java and Spring Boot, this backend leverages **clean architecture principles** to ensure modularity, scalability, and maintainability. Its design emphasizes core software engineering concepts, including:

- **Dependency Inversion Principle (DIP):** Decoupling high-level modules from low-level data access implementations through interfaces.
- **DRY (Don't Repeat Yourself):** Reusing components such as mappers, repositories, and configurations to avoid redundancy and repetition.
- **Design Patterns:**
    - **Strategy Pattern:** Allows the switching of the current running algorithm with another one, during runtime
    - **Adapter Pattern:** Encapsulates external data and translates them into internal domain models.
    - **Bridge Pattern:** Decouples abstraction (i.e. gateway interfaces) from implementations, enabling flexible data source switching (i.e. Firebase, JPA).
    - **Facade Pattern:** Provides simplified interfaces (e.g., `LessonProgressFacade`) to coordinate complex subsystem interactions, hiding implementation details away.
    - **Singleton Pattern:** Holds shared, single instances of stateless components like mappers, configuration classes and repositories for efficient resource management and higher security.

Thanks to its **modular and layered architecture**, the platform can easily be adapted to different data store implementations and deployment scenarios, supporting extensibility and scalability. Because of this, testability is high and allows for extensive testing in each layer.

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

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8.0 or higher
- Firebase credentials and project setup
- Configure environment variables or `.properties` files

### To install, use `npm install` to install all the required dependencies, then `npm run dev` to run the backend application.