# Teacher-Student Collaboration Portal - Backend

This is the backend service for the **Teacher-Student Collaboration Portal**, built using **Spring Boot**. It provides RESTful APIs for task management, including user authentication and authorization.

## Features

- **CRUD operations**: Create, read, update, and delete tasks.  
- **User authentication and authorization**: Secure access using **Spring Security** and **JWT**.  
- **Role-based access control**: Restrict access based on user roles.  
- **RESTful API**: Well-structured API endpoints for seamless integration.  
- **Spring Data JPA & MySQL**: Efficient database management.  

## Tech Stack

- **Spring Boot**  
- **Spring Security (JWT Authentication)**  
- **Spring Data JPA**  
- **MySQL**  
- **Hibernate**  
- **Maven**  

## Installation & Setup

### Prerequisites
- Java 17+  
- MySQL  
- Maven  

### Steps to Run

1. **Clone the repository**  
   ```bash
   git clone https://github.com/Pachpileaditya/Teacher-Student-Collaboration-Portal
   cd Teacher-Student-Collaboration-Portal

2. Configure Database
   Update application.properties with your MySQL database credentials:
   spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
   spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update

3. Build and Run
   mvn clean install
   mvn spring-boot:run

