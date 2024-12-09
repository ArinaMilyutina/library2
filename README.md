# **Library**

The library management system is designed to manage information about books. The system allows you to register, authorize a user, add, update, delete and view information about books.

## **Technology stack:**
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Security(JWT)
- MapStruct
- Maven
- JUnit
- Mockito
- Lombok
- Swagger
- Docker

## **Run the project**
1. git clone https://github.com/ArinaMilyutina/library2
2. cd library2
3. mvn clean install
4. docker-compose up -d --build
5. docker-compose ps (library2-app-1, library2-postgres-1 )

## **Collection of request for testing (Postman):**
1. POST, URL:http://localhost:8080/user/reg/admin
json:
   {
   "username":"Arisha20",
   "password":"123As76y",
   "name":"Arina"
   }

2. POST, URL:http://localhost:8080/user/reg
   json:
   {
   "username":"Arisha21",
   "password":"123As76y",
   "name":"Arina"
   }

3. POST, URL:http://localhost:8080/user/login
   json:
   {
   "username":"Arisha21",
   "password":"123As76y"
   }

4. POST, URL:http://localhost:8080/book/admin/create
Headers: 
Key:Authorization
Value:Bearer_ token(after login)
   json:
   {
   "title": "The Little Prince",
   "author": "Antoine de Saint-Exupery",
   "description": "The fairy tale tells about a Little Prince who visit various planets in space, including Earth",
   "isbn": "1234567890123",
   "genre": ["TALE","FANTASY"]
   }

5.  GET, URL:http://localhost:8080/book/books
    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)

6. GET, URL:http://localhost:8080/book/{id}
   Headers:
   Key:Authorization
   Value:Bearer_ token(after login)

7. GET, URL:http://localhost:8080/book/admin/ISBN/{ISBN}
   Headers:
   Key:Authorization
   Value:Bearer_ token(after login)

8. DELETE, URL:http://localhost:8080/book/admin/delete/{ISBN}
   Headers:
   Key:Authorization
   Value:Bearer_ token(after login)

9. PUT, URL:http://localhost:8080/book/admin/update/{ISBN}
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

10. POST, URL:http://localhost:8080/library/admin/create?username={username}&isbn={ISBN}
    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)
    json:
    {
    "returnDate": "2025-03-05T14:30:00"
    }

11. GET, URL:http://localhost:8080/library/available-books
    Headers:
    Key:Authorization
    Value:Bearer_ token(after login)
12. http://localhost:8080/swagger-ui.html,
    http://localhost:8080/v3/api-docs