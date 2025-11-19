# User Management Service

## 📍 Project Overview

This project is a RESTful User Management Service built with Spring Boot. It provides a set of endpoints to perform CRUD (Create, Read, Update, Delete) operations on user data. The service is containerized using Docker for easy setup and deployment.

### Tech Stack
- **Framework**: Spring Boot 3+
- **Language**: Java 17
- **Database**: PostgreSQL
- **Containerization**: Docker, Docker Compose
- **ORM**: Spring Data JPA (Hibernate)
- **Object Mapping**: MapStruct
- **Build Tool**: Gradle

---

## 📍 Local Development Setup

Follow these steps to run the project on your local machine.

### 1️⃣ Prerequisites

Ensure you have the following installed:
- [Java 17+](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Docker](https://www.docker.com/products/docker-desktop/) & [Docker Compose](https://docs.docker.com/compose/install/)
- [Gradle](https://gradle.org/install/) (Optional, as the project uses the Gradle Wrapper)

### 2️⃣ Clone Repository

```bash
git clone https://github.com/eljanabdullazada/usermanagement.git
cd usermanagement
```

### 3️⃣ Create `.env` File

Create a file named `.env` in the root of the project with the following content:

```env
SERVER_PORT=8081
DB_URL=jdbc:postgresql://db:5432/user_management_db
DB_USERNAME=user_manager
DB_PASSWORD=passWord_for_ums
```

### 4️⃣ Run with Docker

The simplest way to run the application is by using Docker Compose. This will build the Spring Boot application image and start both the application and the PostgreSQL database containers.

```bash
docker compose up --build
```

### 5️⃣ Access the Application

Once the containers are running, the application will be available at:
[http://localhost:8081/api/users/health](http://localhost:8081/api/users/health)

---

## 📍 Running Without Docker

If you prefer to run the application without Docker, you will need to manage the PostgreSQL database and run the Spring Boot application manually.

### 1. Run PostgreSQL Manually

Ensure you have a PostgreSQL instance running and that it is accessible from the application. You can install it locally or use a cloud provider. Update the `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` values in an `application.properties` file or as environment variables accordingly.

### 2. Run the Spring Boot Application

Use the Gradle Wrapper to run the Spring Boot application:

```bash
gradle bootRun
```

---

## 📍 Database Migration

The database schema is managed via Hibernate. For local development, `spring.jpa.hibernate.ddl-auto` is set to `create-drop`, which automatically creates the schema on startup and drops it on shutdown.

For production environments, `ddl-auto` is set to `none`. The database schema must be managed manually or with a dedicated migration tool like Flyway or Liquibase.(I have not used migrations)

---

## 📍 Online Deployment

This service is deployed on Render and is publicly accessible.

**Base URL**: `https://usermanagement-55xb.onrender.com`

You can test the health of the deployed API here:
`https://usermanagement-55xb.onrender.com/api/users/health`

---

## 📍 API Documentation

### Endpoints

| Method   | Endpoint          | Description                                |
|----------|-------------------|--------------------------------------------|
| `POST`   | `/api/users`      | Creates a new user.                        |
| `GET`    | `/api/users/{id}` | Retrieves a single user by their ID.       |
| `GET`    | `/api/users`      | Retrieves a paginated list of all users.   |
| `PUT`    | `/api/users/{id}` | Updates an existing user's information.    |
| `DELETE` | `/api/users/{id}` | Deletes a user by their ID.                |
| `GET`    | `/api/users/health`| Checks the health status of the API (returns 200 OK). |


### Pagination

The `/api/users` endpoint supports pagination to allow clients to retrieve the list of users in manageable chunks. You can control the pagination using the following query parameters:

- `page`: The page number to retrieve (0-indexed).
- `size`: The number of items to include per page.
- `sortBy`: The field to sort the results by (e.g., `id`, `name`, `email`).

**Example Pagination Request:**

To get the second page with 2 users per page, sorted by their ID, you can use the following URL:

```
https://usermanagement-55xb.onrender.com/api/users?page=1&size=2&sortBy=id
```

To get the first page with 4 users per page, sorted by their ID, you can use the following URL:

```
https://usermanagement-55xb.onrender.com/api/users?page=0&size=4&sortBy=id
```

### Request/Response Examples

**Example Create/Update Request Body:**
```json
{
    "name": "Bob Smith",
    "email": "bob.smith@gmail.com",
    "phone": "+1-555-2020",
    "role": "ROLE_ADMIN"
}
```

**Example Successful Create Response:**
```json
{
    "id": "c1f7a2a0-8b9a-4f5e-b8d9-2c3b4a5e6f7d",
    "name": "Bob Smith",
    "email": "bob.smith@gmail.com",
    "phone": "+1-555-2020",
    "role": "ROLE_ADMIN",
    "createdAt": "2025-11-19T10:00:00.000Z"
}
```

### Sample cURL Commands

Replace the placeholder URL with the deployment URL.

**Create a new user:**
```bash
curl -X POST https://usermanagement-55xb.onrender.com/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob Smith","email":"bob.smith@gmail.com","phone":"+1-555-2020","role":"ROLE_USER"}'
```

**Get a list of users:**
```bash
curl -X GET https://usermanagement-55xb.onrender.com/api/users
```

---

## 📍 Error Handling

The API returns standard HTTP status codes to indicate the success or failure of a request.

| Status Code         | Error Type      | Description                                          |
|---------------------|-----------------|------------------------------------------------------|
| `400 BAD REQUEST`   | Validation Error| The request body is invalid (e.g., missing required fields, invalid email format).|
| `404 NOT FOUND`     | Resource Missing| The requested user with the specified ID does not exist. |
| `409 CONFLICT`      | Duplicate Data  | A user with the provided email already exists.       |

**Example Error Response (`400 BAD REQUEST`):**
```json
{
    "timestamp": "2025-11-19T10:15:00.000Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation error: Email must be a valid email address.",
    "path": "/api/users"
}
```

---

## 📍 Project Structure

The project follows a standard layered architecture, with packages organized by feature and responsibility.

```
src/main/java/org/example/usermanagement/
│
├── controller/   # REST API controllers
│   └── UserController.java
│
├── dto/          # Data Transfer Objects for API requests/responses
│   ├── CreateUpdateUserDto.java
│   └── UserDto.java
│
├── entity/       # JPA entity classes
│   ├── User.java
│   └── UserRole.java
│
├── exception/    # Custom exception classes and global handler
│   ├── EmailAlreadyExistsException.java
│   ├── GlobalExceptionHandler.java
│   └── NotFoundException.java
│
├── mapper/       # MapStruct object mappers
│   └── UserMapper.java
│
├── repository/   # Spring Data JPA repositories
│   └── UserRepository.java
│
└── service/      # Business logic and service layer
    ├── impl/
    │   └── UserServiceImpl.java
    └── UserService.java
```

---
