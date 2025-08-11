# Read Me First

* Better do a `mvn clean install` before running the application to ensure all dependencies are resolved and the project is built correctly.
* I have avoided the fault tolerance features of batch processing, such as retry and skip, to keep the project simple
  and banking transactions are sensitive to these features.
* The project uses an in-memory H2 database for demonstration purposes, but it can be easily configured to use other
  databases like MySQL or PostgreSQL by changing the `application.yml` file.
* Assuming that the input file is in CSV format, and the data types are valid. Only file input header row validation is
  implemented, and no other validations are performed on the input data.
* If validation needed can implement a custom data processor to validate the input data.
* The project uses Lombok to reduce boilerplate code, such as getters, setters, and constructors. Make sure to
  have the Lombok plugin installed in your IDE for better development experience.
* Mapstruct is used to map between different object types, such as mapping from CSV data to entity objects.
* Swagger is used to document the RESTful web service endpoints, making it easier to understand and test the API.
* Application properties are configured in `src/main/resources/application.yml`, where you can set up database
  connections, logging levels, and other configurations.
* Used SLF4J for logging, which is a simple facade for various logging frameworks. You can configure the logging
  framework of your choice in the `application.yml` file.
* The project uses Java 21 features, so make sure you have the correct JDK version installed.
* Spring validation is used to validate the input data, ensuring that the data meets the required constraints before
  processing it.
* Jacoco is used for code coverage, and you can run the tests to see the coverage report.
* All the request are logged via a rest controller advice, which can be found in the `RequestLoggingControllerAdvice.java` file.
* The exception handling is done using a global exception handler, which can be found in the `GlobalExceptionHandler.java` file.

### API Documentation
Access swagger UI at `http://localhost:8080/swagger-ui/index.html` to explore the API endpoints.

## Design Patterns Used

This project follows several industry-standard design patterns to ensure maintainability, scalability, and testability.

### 1. Layered Architecture (N-Tier Pattern)
- **Controller** – Handles HTTP requests and responses.
- **Service** – Contains business logic.
- **Repository** – Manages database access.
- **Why?**  
  Separating concerns allows each layer to evolve independently and simplifies testing.

### 2. Repository Pattern
- Implemented via Spring Data JPA `Repository` interfaces.
- **Why?**  
  Abstracts database operations, making it easy to switch or modify the persistence layer without affecting business logic.

### 3. Data Transfer Object (DTO) Pattern
- DTOs (`model.dto` package) carry data between layers without exposing entity details.
- **Why?**  
  Improves security, reduces coupling, and allows API-specific payloads.

### 4. Mapper Pattern
- `mapper` package converts between Entity ↔ DTO (manual or MapStruct).
- **Why?**  
  Centralizes conversion logic and avoids repetitive boilerplate code.

### 5. Specification Pattern
- `specification` package builds dynamic, type-safe queries.
- **Why?**  
  Allows flexible filtering and query composition without hardcoding SQL/JPQL.

### 6. Builder Pattern
- Used via Lombok’s `@Builder` annotation for entity and DTO creation.
- **Why?**  
  Makes object creation readable and reduces constructor overloads.

### 7. Aspect-Oriented Programming (AOP) Pattern
- `advise` package contains aspects for cross-cutting concerns such as logging, exception handling, and transaction management.
- **Why?**  
  Keeps business logic clean by separating reusable concerns (e.g., logging, auditing, security checks) into dedicated aspects, improving maintainability and consistency.

---

**Benefits of Using These Patterns**
- Clear separation of concerns.
- Easier unit and integration testing.
- Scalable architecture that adapts to future requirements.
- Reduced code duplication and better maintainability.
- Centralized handling of cross-cutting concerns with AOP.


# Getting Started

* In this project, we use the [Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/3.5.4/maven-plugin) to build
  and run the application.
* In this project, we use Java 21 as the target version.
* The project is configured to use the [Spring Batch](https://docs.spring.io/spring-boot/3.5.4/how-to/batch.html)
  framework for batch processing.
* The project is set up to
  use [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.4/reference/data/sql.html#data.sql.jpa-and-spring-data)
  for database interactions.
* The project includes a RESTful web service
  using [Spring Web](https://docs.spring.io/spring-boot/3.5.4/reference/web/servlet.html).