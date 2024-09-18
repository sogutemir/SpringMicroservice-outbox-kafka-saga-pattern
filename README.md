# Food Ordering System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)


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
- **Reliable Messaging with Outbox Pattern**: Ensures consistent and reliable message delivery between services.

## Architecture

The project follows the **Hexagonal Architecture (Ports and Adapters)** pattern, emphasizing **Domain-Driven Design (DDD)** and **Clean Architecture** principles. This ensures a clear separation of concerns and promotes scalability and maintainability.

- **Domain Layer**: Contains business logic and domain entities.
- **Application Layer**: Coordinates application activities and use cases.
- **Infrastructure Layer**: Deals with external systems like databases and messaging.
- **Outbox Pattern Implementation**: Ensures reliable communication between microservices by persisting messages in an outbox table in the database and then publishing them to the message broker.

## Technologies Used

- **Programming Language**: Java 17
- **Frameworks and Libraries**:
    - Spring Boot
    - Spring Data JPA
    - Spring Transaction Management
- **Messaging and Streaming**: Apache Kafka
- **Database**: PostgreSQL
- **Containerization**: Docker
- **Build Tool**: Maven

## Project Structure

The project is modularized, reflecting its microservices architecture:

### Common Modules

- **`common`**: Shared utilities and code across microservices.
- **`infrastructure`**: Infrastructure components like Kafka configurations and Outbox pattern implementations.

### Microservice Modules

Each microservice comprises:

- **`domain`**: Domain models and core business logic.
- **`application`**: Application services and use cases.
- **`dataaccess`**: Data repositories and database interactions.
- **`messaging`**: Kafka producers and consumers.
- **`outbox`**: Components related to the Outbox pattern for reliable message handling.
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

2. **Navigate to the Project Directory**

   ```bash
   cd FoodOrderingSystem
   ```

3. **Build the Project**

   ```bash
   mvn clean package
   ```

4. **Start Docker Containers**

   Ensure that Docker is running, then start the required services:

   ```bash
   docker-compose -f common.yml -f init_kafka.yml up -d
   docker-compose -f common.yml -f kafka_cluster.yml up -d
   docker-compose -f common.yml -f zookeeper.yml up -d
   ```

5. **Run Microservices**

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
2. **Outbox Logging**: The **Order Service** saves the order and writes an event to the outbox table within the same transaction.
3. **Message Dispatch**: A background process reads events from the outbox table and publishes them to **Apache Kafka**.
4. **Payment Processing**: The **Payment Service** consumes the event from Kafka, processes the payment, and uses the Outbox pattern to communicate back.
5. **Order Approval**: The **Restaurant Service** receives the event, approves the order, and updates the order status.
6. **Notification**: The customer receives updates on the order status.

_By utilizing the Outbox pattern, we ensure that message delivery between microservices is reliable and consistent, maintaining data integrity across services._

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
    - Implement Outbox entities and repositories if necessary.

4. **Messaging**

    - Update Kafka producers and consumers.
    - Define new topics if necessary.
    - Ensure messages are written to the Outbox table within the same transaction as the domain event.

5. **API Layer**

    - Update controllers.
    - Define new endpoints.

### Testing

- **Unit Tests**: Use JUnit and Mockito.
- **Integration Tests**: Use Testcontainers for databases and Kafka.
- **End-to-End Tests**: Validate the complete workflow.
- **Outbox Pattern Tests**: Ensure that events are correctly written to and read from the outbox table.

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
