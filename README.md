# Parking Lot Management System

A robust RESTful API built with Spring Boot for managing a parking facility. This system handles vehicle check-ins, automated slot allocation based on vehicle types, check-outs with dynamic fee calculation, and comprehensive parking lot administration.

## Features

* **Automated Slot Allocation:** Automatically assigns the most suitable available parking slot during check-in based on the vehicle type (Car, Truck, Motorcycle).
* **Dynamic Fee Calculation:** Uses the Strategy Design Pattern to calculate parking fees differently depending on the vehicle type and duration of stay.
* **Session Management:** Tracks active parking sessions and ensures no duplicate tickets exist for the same vehicle.
* **Hierarchical Facility Management:** Supports creating and managing the parking lot, its levels, and individual slots.
* **Robust Error Handling:** Features a custom exception hierarchy (`ResourceNotFoundException`, `ParkingConflictException`, `InvalidParkingRequestException`) caught by a unified `@RestControllerAdvice` to return standardized, predictable API responses.
* **Data Validation:** Strict input validation at the controller level using `jakarta.validation` annotations.

## Tech Stack

* **Java 21** 
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **Lombok** (Boilerplate reduction, `@StandardException`)
* **Database:** PostgreSQL / H2 (In-memory for testing)
* **Testing:** JUnit 5, Mockito, AssertJ, Spring MockMvc

## Architecture & Design Patterns

This project follows clean architecture principles and implements several GoF design patterns:
* **Strategy Pattern:** Implemented via `FeeCalculationStrategy` to allow scalable and open/closed compliant fee calculations for different vehicle types.
* **Factory Method Pattern:** `VehicleFactory` handles the instantiation of specific vehicle entities.
* **Controller-Service-Repository:** Standard Spring architectural separation of concerns.
* **DTO Pattern:** Clear separation between internal entities and external API contracts using MapStruct/Custom Mappers.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 17 or higher
* Maven 3.6+

### Installation & Running
1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/parking-lot-system.git](https://github.com/your-username/parking-lot-system.git)