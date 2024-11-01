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
    - **Dashboard** (`/auth/users`): Accessible only to users with the `ADMIN` role.

### Step 3: Set Up Application Properties

- Configure default login credentials and roles in the `application.yaml` file to allow basic access control.

## Summary

This setup:
- Secures routes based on roles.
- Provides open routes for user registration (`/user/register`), login (`/user/login`), and password recovery (`/user/forgotpassword`).
- Secures `/user/info` for logged-in users and `/admin/users` for admin-only access.

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
           curl --location 'http://localhost:8080/api/v1/users/forgot-password' \
           --header 'Content-Type: application/json' \
           --header 'Cookie: JSESSIONID=D54AD0AED85D3E50D3E34901CB99BB55' \
           --data-raw '{
           "email":"kd.dev@gmail.com"
           }'
          ```
        - Verify OTP
          ```
          curl --location 'http://localhost:8080/api/v1/users/verify_otp' \
          --header 'Content-Type: application/json' \
          --header 'Cookie: JSESSIONID=D54AD0AED85D3E50D3E34901CB99BB55' \
          --data-raw '{
          "email":"kd.dev@gmail.com",
          "otp":"458067"
          }'
         
        - Reset Password
          ```
          curl --location 'http://localhost:8080/api/v1/users/reset_password' \
          --header 'Content-Type: application/json' \
          --header 'Cookie: JSESSIONID=D54AD0AED85D3E50D3E34901CB99BB55' \
          --data-raw '{
          "email":"kd.dev@gmail.com",
          "password":"1234567"
          }'
          ```

        - Update Password
          ```
          curl --location --request PUT 'http://localhost:8080/api/v1/users/update-password' \
          --header 'Content-Type: application/json' \
          --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlSWQiOjEsImVtYWlsIjoiaW5mby5lbWRpeWFAZ21haWwuY29tIiwidXNlcm5hbWUiOiJkZXYuZGV2Iiwic3ViIjoiZGV2LmRldiIsImlhdCI6MTczMDQ1NDg1NCwiZXhwIjoxNzMwNDYyMDU0fQ.bBSbSHynKMtt9Yjqoz7bJoTbq3VoDLRTcQjlVnlyUBxNobOMYng2Acw03enqDGJh2vdqbXnHBJZu0ovvKGnmBw' \
          --data '{
          "currentPassword": "1234567",
          "newPassword": "123456789",
          "confirmPassword": "123456789"
          }'
          ``` 
        - User Info
          ```
          curl --location 'http://localhost:8080/api/v1/users/info' \
          --header 'Authorization: Bearer eyJhbGciOiJ]IUzUxMiJ9.eyJyb2xlSWQiOjEsImVtYWlsIjoiaW5mby5lbWRpeWFAZ21haWwuY29tIiwidXNlcm5hbWUiOiJkZXYuZGV2Iiwic3ViIjoiZGV2LmRldiIsImlhdCI6MTczMDQ4MDk4NSwiZXhwIjoxNzMwNDg4MTg1fQ.Oh6jaDIBNbWZPDNxwEAZgtSEZoOXyr4zdhdtGWQBN2JJOasBBTHUj15MDjyDMWSdCPFNZNLBjefmEHnNl5h1eA' \
          --header 'Cookie: JSESSIONID=D54AD0AED85D3E50D3E34901CB99BB55'
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
            --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlSWQiOjQsImVtYWlsIjoia2QuYWRtaW5AZ21haWwuY29tIiwidXNlcm5hbWUiOiJrZC5hZG1pbiIsInN1YiI6ImtkLmFkbWluIiwiaWF0IjoxNzMwMjc5MzY2LCJleHAiOjE3MzAyODY1NjZ9.ROMuNsCYrkOCWfbkOm7HtjQw__XtPKfCoCJGvqXE8mkq0o1K8y83msB3uTtn2LA0k8JJvM0Rbtk0DHgiRr2glw' \
            --header 'Cookie: JSESSIONID=D54AD0AED85D3E50D3E34901CB99BB55'
            ```

4. **Done**