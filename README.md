# Crewmeister Java Coding Challenge


## Requirements

- Java 17
- Maven 3.x


## Running the Application

In order to build and run the application, you just need to click the green arrow in the `CmCodingChallengeApplication` class in IntelliJ or run the following command from the shell.
```shell
$ mvn spring-boot:run
```

After starting the project, switch to your browser and open http://localhost:8080/api/v1/currencies.

Alternatively, you can explore the API using the Swagger UI at http://localhost:8080/swagger-ui/index.html.


## Use Cases


### As a client, I want to get a list of all available currencies

To get all available currencies, use the following query:  
> http://localhost:8080/api/v1/currencies


### As a client, I want to get all EUR-FX exchange rates at all available dates as a collection

To get all exchange rates at all available dates, use the following query:  
> http://localhost:8080/api/v1/rates


### As a client, I want to get the EUR-FX exchange rate at particular day

To get all exchange rates on, for example, 2023-08-04, use the following query:  
> http://localhost:8080/api/v1/rates?date=2023-08-04
 
To get the exchange rate on, for example, 2023-08-04 for the USD, use the following query:  
> http://localhost:8080/api/v1/rates?currency=USD&date=2023-08-04


### As a client, I want to get a foreign exchange amount for a given currency converted to EUR on a particular day

To convert, for example, AUD 200 to EUR on 1999-06-23, first find the rate ID with the following query:  
> http://localhost:8080/api/v1/rates?currency=AUD&date=1999-06-23

Then, use the `conversion` hyperlink returned by the query above to find a conversion. Assuming the query above returned a rate with ID 123, then to convert, for example, AUD 200 against this rate the query would look like this: 
> http://localhost:8080/api/v1/rates/123/conversion?foreignAmount=200
