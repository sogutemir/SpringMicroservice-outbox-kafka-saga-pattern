# Food Ordering System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
  - [API Endpoints](#api-endpoints)
  - [Sample Request](#sample-request)
- [Data Flow](#data-flow)
- [Development Guidelines](#development-guidelines)
  - [Adding a New Feature](#adding-a-new-feature)
  - [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

**Food Ordering System** is a backend application built using a microservices architecture to facilitate seamless and efficient food ordering processes. Customers can place orders from various restaurants, make secure payments, and receive real-time updates, while restaurants can manage orders and menus effectively.

_This README will be continuously updated to reflect the latest changes and improvements as the project progresses._

## Features

- **Order Management**: Place, track, and manage food orders.
- **Payment Processing**: Secure and reliable payment transactions.
- **Restaurant Interface**: Restaurants can approve or reject orders.
- **Customer Profiles**: Manage customer information and order history.
- **Real-Time Notifications**: Updates via Apache Kafka messaging.

## Architecture

The project follows the **Hexagonal Architecture (Ports and Adapters)** pattern, emphasizing **Domain-Driven Design (DDD)** and **Clean Architecture** principles. This ensures a clear separation of concerns and promotes scalability and maintainability.

- **Domain Layer**: Contains business logic and domain entities.
- **Application Layer**: Coordinates application activities and use cases.
- **Infrastructure Layer**: Deals with external systems like databases and messaging.

## Technologies Used

- **Programming Language**: Java 17
- **Frameworks and Libraries**:
  - Spring Boot
  - Spring Data JPA
- **Messaging and Streaming**: Apache Kafka
- **Database**: PostgreSQL
- **Containerization**: Docker
- **Build Tool**: Maven

## Project Structure

The project is modularized, reflecting its microservices architecture:

### Common Modules

- **`common`**: Shared utilities and code across microservices.
- **`infrastructure`**: Infrastructure components like Kafka configurations.

### Microservice Modules

Each microservice comprises:

- **`domain`**: Domain models and core business logic.
- **`application`**: Application services and use cases.
- **`dataaccess`**: Data repositories and database interactions.
- **`messaging`**: Kafka producers and consumers.
- **`container`**: Spring Boot application entry point.

## Installation and Setup

### Prerequisites

- **Java Development Kit (JDK) 17**
- **Maven**
- **Docker & Docker Compose**

### Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/sogutemir/FoodOrderingSystem.git
   ```

1. **Navigate to the Project Directory**

   ```bash
   cd FoodOrderingSystem
   ```

2. **Build the Project**

   ```bash
   mvn clean package
   ```

3. **Start Docker Containers**

   Ensure that Docker is running, then start the required services:

   ```bash
   docker-compose -f common.yml -f init_kafka.yml up -d
   docker-compose -f common.yml -f kafka_cluster.yml up -d
   docker-compose -f common.yml -f zookeeper.yml up -d
   ```

4. **Run Microservices**

   Open separate terminals for each microservice and run:

   ```bash
   # Order Service
   java -jar order-service/order-container/target/order-container-1.0-SNAPSHOT.jar

   # Payment Service
   java -jar payment-service/payment-container/target/payment-container-1.0-SNAPSHOT.jar

   # Restaurant Service
   java -jar restaurant-service/restaurant-container/target/restaurant-container-1.0-SNAPSHOT.jar

   # Customer Service
   java -jar customer-service/customer-container/target/customer-container-1.0-SNAPSHOT.jar
   ```

## Usage

### API Endpoints

The application exposes RESTful APIs for interaction. Here are some of the key endpoints:

- **Order Service**
    - Create Order: `POST /orders`
    - Get Order Details: `GET /orders/{orderId}`
    - Update Order: `PUT /orders/{orderId}`
    - Delete Order: `DELETE /orders/{orderId}`
- **Payment Service**
    - Process Payment: `POST /payments`
- **Restaurant Service**
    - Approve Order: `POST /restaurants/{restaurantId}/orders/{orderId}/approve`
    - Reject Order: `POST /restaurants/{restaurantId}/orders/{orderId}/reject`
- **Customer Service**
    - Register Customer: `POST /customers`
    - Get Customer Info: `GET /customers/{customerId}`

### Sample Request

**Create Order**

**Endpoint:** `POST http://localhost:8181/orders`

**Headers:**

- `Content-Type: application/json`

**Request Body:**

```json
{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "address": {
    "street": "Main Street",
    "postalCode": "12345",
    "city": "Amsterdam"
  },
  "price": 200.00,
  "items": [
    {
      "productId": "product-1",
      "quantity": 1,
      "price": 50.00,
      "subTotal": 50.00
    },
    {
      "productId": "product-2",
      "quantity": 3,
      "price": 50.00,
      "subTotal": 150.00
    }
  ]
}
```

**Response:**

```json
{
  "orderId": "order-123",
  "status": "PENDING",
  "message": "Order created successfully."
}
```

For detailed API documentation, refer to the [API Documentation](https://github.com/sogutemir/FoodOrderingSystem/wiki).

## Data Flow

1. **Order Placement**: Customer places an order via the **Order Service**.
2. **Payment Initiation**: **Order Service** sends order details to the **Payment Service**.
3. **Payment Confirmation**: **Payment Service** processes payment and notifies the **Order Service**.
4. **Order Approval**: Order is forwarded to the **Restaurant Service** for approval.
5. **Notification**: Customer receives updates on the order status.

## Development Guidelines

### Adding a New Feature

1. **Domain Layer**

    - Define new entities or value objects.
    - Implement business logic and rules.

2. **Application Layer**

    - Create or update application services.
    - Define use cases.

3. **Data Access Layer**

    - Add or modify repository interfaces.
    - Implement data access logic.

4. **Messaging**

    - Update Kafka producers and consumers.
    - Define new topics if necessary.

5. **API Layer**

    - Update controllers.
    - Define new endpoints.

### Testing

- **Unit Tests**: Use JUnit and Mockito.
- **Integration Tests**: Use Testcontainers for databases and Kafka.
- **End-to-End Tests**: Validate the complete workflow.

## Contributing

We welcome contributions! To contribute:

1. **Fork the Project**

   ```bash
   git clone https://github.com/your-username/FoodOrderingSystem.git
   ```

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/YourFeatureName
   ```

3. **Commit Your Changes**

   ```bash
   git commit -m "Add YourFeatureName"
   ```

4. **Push to the Branch**

   ```bash
   git push origin feature/YourFeatureName
   ```

5. **Open a Pull Request**

   Submit your pull request, and we will review it as soon as possible.

## License

This project is licensed under the **MIT License**. See the [LICENSE](https://github.com/sogutemir/FoodOrderingSystem/blob/main/LICENSE) file for details.

## Contact

**Emir SoGood**

- **GitHub**: [@sogutemir](https://github.com/sogutemir)
- **Email**: [sogutemir72@gmail.com](mailto:sogutemir72@gmail.com)

Project Link: [FoodOrderingSystem](https://github.com/sogutemir/FoodOrderingSystem)

---
