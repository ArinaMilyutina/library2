# **Library**

The application is a library system consisting of 3 microservices. Library2: The service is responsible for managing
users, registering them, authorizing them, and providing necessary user information to other services.
Book-service:The service provides an interface for working with a catalog of books. It manages information about books,
their status, and allows you to perform basic operations with books.
Library-service:The service manages the processes of issuing and returning books by users.

## **Technology stack:**

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Security(JWT)
- MapStruct
- Maven
- Spring Cloud
- Eureka
- JUnit
- Mockito
- Lombok
- Swagger
- Docker

## **Run the project**

1. docker network create my-network
2. git clone https://github.com/ArinaMilyutina/eureka-server
3. cd eureka-server
4. mvn clean install
5. docker build -t eureka-server .
6. docker run -d -p 8761:8761 --name eureka-server eureka-server
7. docker network connect my-network eureka-server
8. git clone https://github.com/ArinaMilyutina/library2
9. cd library2
10. mvn clean install
11. docker-compose up -d --build
12. docker network connect my-network library2-app-1
13. git clone https://github.com/ArinaMilyutina/book-service
14. cd book-service
15. mvn clean install
16. docker-compose up -d --build
17. docker network connect my-network book-service-app-1
18. git clone https://github.com/ArinaMilyutina/library-service
19. cd library-service
20. mvn clean install
21. docker-compose up -d --build
22. docker network connect my-network library-service-app-1

## **Collection of request for testing (Postman):**

##### 1. POST, URL:http://localhost:8081/user/reg/admin

    json:
    {
    "username":"Arisha20",
    "password":"123As76y",
    "name":"Arina"
    }

##### 2. POST, URL:http://localhost:8081/user/reg

    json:
    {
    "username":"Arisha21",
    "password":"123As76y",
    "name":"Arina"
    }

##### 3. POST, URL:http://localhost:8081/user/login

    json:
    {
    "username":"Arisha21",
    "password":"123As76y"
    }

##### 4. POST, URL:http://localhost:8082/book/create

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)
    json:
    {

    "title": "The Little Prince",
    "author": "Antoine de Saint-Exupery",
    "description": "The fairy tale tells about a Little Prince who visit various planets in space, including Earth",
    "isbn": "1234567890123",
    "genre": ["TALE","FANTASY"],
    "status":["AVAILABLE"]
    }

##### 5. GET, URL:http://localhost:8082/book/books

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 6. GET, URL:http://localhost:8082/book/{id}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 7. GET, URL:http://localhost:8082/book/admin/ISBN/{ISBN}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 8. DELETE, URL:http://localhost:8082/book/admin/delete/{ISBN}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 9. PUT, URL:http://localhost:8082/book/admin/update/{ISBN}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)
    json:
    {
    "title": "A Walk to Remember",
    "author": "Nicholas Sparks",
    "description": "Love story with a sad ending",
    "isbn": "1234567890109",
    "genre": ["ROMANCE"]
    }

##### 10. GET, URL:http://localhost:8082/book/available-books

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 11. POST, URL:http://localhost:8083/library/admin/take-book?userId={userId}&bookId={bookId}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)
    json:
    {
    "expectedReturnDate": "2025-03-05T14:30:00"
    }

##### 12. GET, URL:http://localhost:8083/library/admin/return-book/{bookId}

    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

##### 13. Swagger:

    http://localhost:8081/swagger-ui.html,
    http://localhost:8081/v3/api-docs

    http://localhost:8082/swagger-ui.html,
    http://localhost:8082/v3/api-docs

    http://localhost:8083/swagger-ui.html,
    http://localhost:8083/v3/api-docs