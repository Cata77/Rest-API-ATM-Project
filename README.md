# Java Spring Boot Rest API ATM Project
 The application simulates the usage of an ATM via REST API calls. It aims to provide the user with bank operations and 
to keep a record of customer transactions in a database.

## Functionality
The ATM offers three sets of operatons:

- Authentication

  - Register
  
  - Login

- User

  - Deposit
  
  - Withdraw
  
  - Transfer (to another bank user)
  
  - Bank account details
  
  - Bank statement for a certain date
  
  - Close bank account
  
- Bank

  - Bank users
  
  - Transactions
  
  - Accounts
  
  - Bank balance
  
  - Show user with most transactions
  
  - Show user with highest balance
  
  - Transactions occured between two given dates
  
  - Find the date with most transactions
  
 **Observation:** In order for the application flow to run correctly, register the users first.
 
 ## Documentation
 The documentation for each ATM endpoint was done via **Swagger springdoc-openapi** and it can be accessed after running the application at the following
 url ```http://localhost:8080/swagger-ui.html```
 
 ## Technology
 
 - Java 17
 
 - Spring Boot (3.0.1)
 
 - Hibernate 
 
 - Spring Data JPA
 
 - Lombok
 
 - Unit test: Junit 5 + Mockito
 
 - Documentation: Swagger springdoc-openapi
 
 - Endpoints testing: Postman
 
 - Build Tool: Gradle
 
 - Database: PostgreSql
 
 - IDE: IntelliJ
  
  



