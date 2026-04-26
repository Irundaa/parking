# Parking Lot Management System

A robust RESTful API built with Spring Boot for managing a parking facility. This system handles vehicle check-ins, automated slot allocation based on vehicle types, check-outs with dynamic fee calculation, and comprehensive parking lot administration.

---

## Features

* **Automated Slot Allocation:** Automatically assigns the most suitable available parking slot during check-in based on the vehicle type (Car, Truck, Motorcycle).
* **Dynamic Fee Calculation:** Uses the **Strategy Design Pattern** to calculate parking fees differently depending on the vehicle type and duration of stay.
* **Session Management:** Tracks active parking sessions and ensures no duplicate tickets exist for the same vehicle.
* **Hierarchical Facility Management:** Supports creating and managing the parking lot, its levels, and individual slots.
* **Robust Error Handling:** Features a custom exception hierarchy (`ResourceNotFoundException`, `ParkingConflictException`, `InvalidParkingRequestException`) caught by a unified `@RestControllerAdvice` to return standardized, predictable API responses.
* **Data Validation:** Strict input validation at the controller level using `jakarta.validation` annotations.

---

## Tech Stack

* **Java 21** (LTS)
* **Spring Boot 3.4.1** (Web, Data JPA, Validation)
* **Gradle 8.14.4**
* **MySQL 8.0** (Database)
* **Lombok & MapStruct** (Boilerplate reduction and object mapping)
* **Docker & Docker Compose** (Containerization for the database environment)
* **Checkstyle** (Google Java Style)
* **JaCoCo** (Code coverage verification)
* **Swagger/OpenAPI 3.0** (Interactive API documentation)

---

## Code Quality

To maintain high development standards, the project includes:
* **Checkstyle:** Automatically verifies code style against **Google Java Style** conventions.
* **JaCoCo:** Measures code coverage by tests. The current threshold is set to **80%**.
* **Global Exception Handling:** Centralized error management for predictable and consistent API responses.

---

## Architecture & Design Patterns

The project follows clean architecture principles and implements several GoF design patterns:
* **Strategy Pattern:** Implemented via `FeeCalculationStrategy` to allow scalable and open/closed compliant fee calculations for different vehicle types.
* **Factory Method Pattern:** `VehicleFactory` handles the instantiation of specific vehicle entities.
* **Controller-Service-Repository:** Standard Spring architectural separation of concerns.
* **DTO Pattern:** Clear separation between internal entities and external API contracts using MapStruct.
* **Dependency Injection:** Constructor-based injection managed by Spring (facilitated by Lombok's `@RequiredArgsConstructor`).

---

## Getting Started

### Prerequisites
* **JDK 21** or higher
* **Docker Desktop** installed and running

### Installation & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Irundaa/parking.git
   ```

2. **Start the database using Docker:**
   ```bash
   docker-compose up -d
   ```

3. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

4. **Run quality checks (Tests, Checkstyle, and JaCoCo):**
   ```bash
   ./gradlew check
   ```