# S4.03 — Fruit Orders API 🍏📦

## 📄 Description

This project is a **REST API built with Spring Boot** for managing **fruit orders** made by clients.  
Each order contains:

- The client's name
- A delivery date
- A list of ordered fruits with quantity in kilograms

Data is stored in **MongoDB**, using a single collection with embedded documents.

The project follows:

- **Layered architecture (Controller → Service → Repository)**
- **DTOs and Value Objects**
- **Bean Validation (@Valid)**
- **Custom exceptions + GlobalExceptionHandler**
- **Full TDD workflow**: RED → GREEN → REFACTOR
- **Unit tests** for the service
- **Integration tests** using MockMvc + real MongoDB (test DB)

---

## 💻 Technologies Used

- **Java 21**
- **Spring Boot 3.x**
    - Spring Web
    - Spring Data MongoDB
    - Validation API (Jakarta)
- **MongoDB (local or Docker)**
- **Maven**
- **JUnit 5 / Mockito**
- **MockMvc**
- **Docker** (optional)
- **Testcontainers** (optional)

---

## 📋 Requirements

| Tool | Version |
|------|---------|
| Java | 21 |
| Maven | 3.9+ |
| MongoDB | 7+ (or Docker container) |
| Docker | 24+ (optional) |

---

## 🛠️ Installation & Setup

### 1️⃣ Clone the repository
```bash
git clone https://github.com/leilaweicman/4.2-api-rest-with-spring-mongodb.git
cd fruit-order-api
```

### 2️⃣ Start MongoDB (Docker)
```bash
docker run -d   --name fruit-mongo   -p 27017:27017   -v ./mongo-data:/data/db   mongo:latest
```

### 3️⃣ Build the project
```bash
mvn clean package
```

### 4️⃣ Run the application
```bash
java -jar target/fruit-order-api-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Available Endpoints

| Method | Endpoint | Description | Response |
|--------|-----------|--------------|-----------|
| **POST** | `/orders` | Create a new order | `201 Created` |
| **GET** | `/orders` | Retrieve all orders | `200 OK` |
| **GET** | `/orders/{id}` | Retrieve a specific order | `200 OK` or `404 Not Found` |
| **PUT** | `/orders/{id}` | Update an existing order | `200 OK` or `404 Not Found` |
| **DELETE** | `/orders/{id}` | Delete a specific order | `204 No Content` or `404 Not Found` |

---

## 📦 MongoDB Order Model

### **Order Document**
```json
{
  "id": "ObjectId",
  "clientName": "John",
  "deliveryDate": "2025-12-12",
  "items": [
    { "fruitName": "Apple", "quantityInKilos": 3 }
  ]
}
```

### **OrderItem Embedded Document**
- fruitName (String)
- quantityInKilos (int > 0)

---

## 🧪 Testing

### 🧩 Unit Tests (Service Layer)

Written using:

- **Mockito** for repository + mapper
- **JUnit 5**

Coverage includes:

- Creating orders
- Updating orders
- Deleting orders
- Fetching one or all orders
- Error paths (404, invalid data)

TDD followed strictly:

```
RED → GREEN → REFACTOR
```

---

### 🔍 Integration Tests (Controller)

Using **@SpringBootTest + MockMvc** with **real MongoDB** (test collection):

- POST /orders → status + JSON assertions
- GET /orders → list sizes, field checks
- GET /orders/{id} → 200 or 404
- PUT /orders/{id} → update validation
- DELETE /orders/{id} → verifies record deletion

Every test:

- Cleans MongoDB before running
- Uses JSON strings (no ObjectMapper needed)

---

## 🧾 Global Exception Handling

All errors are handled by `GlobalExceptionHandler`, returning consistent JSON responses.

### Example error response:

```json
{
  "timestamp": "2025-12-12T10:15:32",
  "status": 400,
  "error": "Validation Error",
  "message": "Client name cannot be blank",
  "path": "/orders"
}
```

Handled exceptions include:

- `NotFoundException`
- `BadRequestException`
- `MethodArgumentNotValidException`
- Custom validation errors

---

## 🧩 DTO and Validation

DTOs are implemented as **records** for immutability.

### OrderRequest DTO
```java
public record OrderRequest(
    @NotBlank String clientName,
    @FutureOrPresent LocalDate deliveryDate,
    @Size(min = 1) List<OrderItemRequest> items
) {}
```

### OrderItemRequest
```java
public record OrderItemRequest(
    @NotBlank String fruitName,
    @Positive int quantityInKilos
) {}
```

These ensure all incoming requests are validated before reaching the service layer.

---

## 🐳 Docker Support

A basic Dockerfile for packaging the application:

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build the image
```bash
docker build -t fruit-order-api .
```

### Run the container
```bash
docker run -p 8080:8080 fruit-order-api
```

---

## 💡 Useful Commands

Start Mongo:
```bash
docker start fruit-mongo
```

Run tests:
```bash
mvn test
```

Clean database folder:
```bash
rm -rf mongo-data/*
```

---

## 📚 Summary

This project demonstrates:

- Clean architecture using Service + DTO + Mapper
- MongoDB with embedded documents
- Strong validation and error handling
- Complete TDD workflow
- High-quality integration tests
- Production-ready structure

---