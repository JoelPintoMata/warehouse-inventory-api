Code challenge
==============

# Tests

`mvn clean test`

# Run

`mvn spring-boot:run`

# API

__Return all products__
```
curl http://localhost:8080/products/availability 
``` 

__Sell product__
DELETE /products/{id}, for example:		
```
curl -X DELETE http://localhost:8080/products/1
```

__Return all articles__
```
curl http://localhost:8080/articles  
``` 

### Spring Data JDBC vs. Spring Data JPA
- Less complex
- Improved performance

### Data model
Check `schema.sql` file under resources folder

