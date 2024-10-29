# Spring Boot Security with User and Admin Roles (JWT)

This guide provides a basic setup for securing a Spring Boot application with role-based access using JWT (JSON Web Tokens), defining specific routes for user and admin roles.

## Prerequisites

- **Spring Boot**: 3.0.x or higher
- **Spring Security**: Integrated as a dependency in the project
- **Java**: JDK 21 or higher
- **Database**: postgresql (pgAdmin4)

## Project Structure

```plaintext
src
├── main
│   ├── java
│   │   ├── config
│   │   │   └── SecurityConfig.java
│   │   ├── controller
│   │   │   └── UserController.java
│   │   ├── model
│   │   │   └── UserModel.java
│   │   ├── repository
│   │   │   └── UserRepository.java
│   │   └── service
│   │       └── UserService.java
│   └── resources
│       └── application.yaml
```

1. **Security Configuration**: Define security rules in a configuration file to control access based on roles.
2. **Controllers**: Create `UserController` and `AuthController` to manage routes for users and admins.
3. **Application Properties**: Add default security configurations in the `application.yaml` file.

## Step-by-Step Setup

### Step 1: Configure Security Roles

- In your security configuration file, set up access rules for each role.
- Allow unrestricted access to `/user/register`, `/user/login`, and `/user/forgotpassword`.
- Restrict `/user/**` routes to users with the `USER` role.
- Restrict `/admin/**` routes to users with the `ADMIN` role.

### Step 2: Define Routes in Controllers

- **User Routes**:
    - **Register** (`/user/register`): Allows users to register.
    - **Login** (`/user/login`): Provides an endpoint for user login.
    - **Forgot Password** (`/user/forgotpassword`): Enables users to reset their password.
    - **User Info** (`/user/info`): Accessible only to logged-in users with the `USER` role.

- **Admin Routes**:
    - **Dashboard** (`/admin/dashboard`): Accessible only to users with the `ADMIN` role.

### Step 3: Set Up Application Properties

- Configure default login credentials and roles in the `application.yaml` file to allow basic access control.

## Summary

This setup:
- Secures routes based on roles.
- Provides open routes for user registration (`/user/register`), login (`/user/login`), and password recovery (`/user/forgotpassword`).
- Secures `/user/info` for logged-in users and `/admin/dashboard` for admin-only access.

This guide enables a structured, role-based access system for both users and admins in a Spring Boot application.


## How to Start the Project

1. **Clone the Repository**: Clone the project repository to your local machine.
   ```bash
   git clone https://github.com/emdiya/spring_user_service.git
   cd spring_user_service
   ```
   
2. **Start running**
    ```
   ./mvnw clean install
   ./mvnw spring-boot:run
    ```
   or Run in IntelliJ IDE...


3. **Start Access Endpoint**
    - *Copy below curl url and past into postman request*
      - **User Routes**:
        - User Register
          ```
            curl --location 'http://localhost:8080/api/v1/users/register' \
            --header 'Content-Type: application/json' \
            --data-raw '{
            "email":"kd.dev@gmail.com",
            "username":"kd.dev",
            "password":"12456"
            }'
          ```
        - User Login
          ```
             curl --location 'http://localhost:8080/api/v1/users/register' \
              --header 'Content-Type: application/json' \
              --data-raw '{
              "email":"kd.dev@gmail.com",
              "username":"kd.dev",
              "password":"12456"
              }'
          ```    
        - Forgot Password
          ```
             curl --location 'http://localhost:8080/api/v1/users/register' \
              --header 'Content-Type: application/json' \
              --data-raw '{
              "email":"kd.dev@gmail.com",
              "username":"kd.dev",
              "password":"12456"
              }'
          ```
      - **Admin Routes**:
        - Login Admin
           ```
           curl --location 'http://localhost:8080/api/v1/auth/login' \
           --header 'Content-Type: application/json' \
           --data '{
           "username":"kd.admin",
           "password":"123456"
           }'
           ```    
        - User
            ```
          curl --location 'http://localhost:8080/api/v1/auth/users' \
          --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJTdXBlciBBZG1pbiJ9XSwiZW1haWwiOiJrZC5hZG1pbkBnbWFpbC5jb20iLCJzdWIiOiJrZC5hZG1pbiIsImlhdCI6MTczMDE4MDgxOCwiZXhwIjoxNzMwMTg4MDE4fQ.QV0C4Mh6pZyjdKZJxWGHx8Q66PK_AvmTJ5BgHhTPcLc5IKOgL_uq7TCIR70zK0gN5aqNw8Zz8lIG4ku_kDO2XA' \
          --header 'Cookie: JSESSIONID=4E8AE246FD5D52BB4F94BC96E7635A45'
            ```

4. **Done**