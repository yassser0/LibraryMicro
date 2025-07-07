Overview
LibraryMicro is a modular microservices platform designed to facilitate scalable API management and service orchestration in modern distributed systems. It leverages Spring Cloud Gateway, WebFlux, and Eureka for reactive, resilient, and discoverable architecture.

Why LibraryMicro?
This project simplifies building and maintaining complex microservices ecosystems. The core features include:
🧩🌐 API Gateway: Reactive, scalable routing with centralized request management.
🧭🚀 Service Discovery: Dynamic registration and discovery via Eureka for seamless inter-service communication. 
🛠️🔧 Microservices: Dedicated modules for user, book, admin, and payment functionalities.
📊📋 RESTful APIs & DTOs: Consistent data exchange and robust CRUD operations.
🔒🛡️ Secure & Modular: Designed for flexibility, security, and easy scalability.

Prerequisites
This project requires the following dependencies:
Programming Language: Java
Package Manager: Maven

Installation
Build LibraryMicro from the source and install dependencies:

1.Clone the repository:
![image](https://github.com/user-attachments/assets/8ade4721-f5d4-46b0-8f7a-e8e1c14bb9f0)
2.Navigate to the project directory: 
![image](https://github.com/user-attachments/assets/801a210c-af0d-416e-83eb-e6ee0c19d44d)
3-to run the whole programme you need to run every service : (discoveryService , user-service ,book-servcie, payment-service , admine-service ,api-gateway ) 
for exemple (user-service) : 
cd user-service 
.\mvnw spring-boot:run 

4-probleme u can have : 
1-database probleme (creat ur database and change it in application.yaml for every service) 
2-different spring version 



