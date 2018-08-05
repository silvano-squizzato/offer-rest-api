public static boolean isValid(Offer offer)
# Offer RESTful API
The Offer RESTful API provides a basic RESTful service to manage offers.
The project is based on the [Spring Boot](https://spring.io/projects/spring-boot) framework which allows to create a stand-alone RESTful application with an in-memory persistence repository.

Once the project is downloaded and built on a machine, on starting the main application the following local endpoint should become available
[http://localhost:8080/offer/1](http://localhost:8080/offer/1)

On the same server running the Offer RESTful APIa a basic dedicated [Swagger](https://swagger.io/) page is also presented under [http://localhost:8080/swagger-ui.html#/offer-controller](http://localhost:8080/swagger-ui.html#/offer-controller).
The Swagger page not only provides documentation to the API methods but also is a powerful means to test on-the-fly the API via a browser.

# Offer charactersitics
A valid offer is specific to one product with a given price.
A JSON example of an offer representation follows below.
```json
{
  "id": 2,
  "product": "TV set",
  "price": 300,
  "start": "2018-09-01",
  "end": "2018-10-29"
}
```
Only one implicit currency is used according to the locale.
The duration of an offer is given by the difference between the end and start date.
In this simple version the granularity of the duration period is in days, and only one timezone is considered.
An offer is expired if the current date is beyond the end date.
An offer can be tracked through its identifier when stored in the system.
In using the RESTful API start and end dates can be specified using the pattern 'yyyy-MM-dd'.

# Usage
curl command examples of the API usage from the Swagger page are listed below.
1. POST /offer Add an offer
```sh
curl -X POST "http://localhost:8080/offer" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"id\": 1, \"product\": \"test\", \"price\": 2, \"start\": \"2018-09-01\", \"end\": \"2018-10-01\"}"
```
2. GET /offer/{id} Get an offer with a given id
```sh
curl -X GET "http://localhost:8080/offer/1" -H "accept: application/json"
```
3. PUT /offer/{id} Update an offer with a given id
```sh
curl -X PUT "http://localhost:8080/offer/1" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"id\": 1, \"product\": \"test\", \"price\": 400, \"start\": \"2018-09-01\", \"end\": \"2018-10-01\"}"
```
4. DELETE /offer/{id} Delete an offer with a given id
```sh
curl -X DELETE "http://localhost:8080/offer/1" -H "accept: application/json"
```

# Build the project
From the source folder of the project type this command
```sh
./mvnw clean package
```

# Basic assumptions
1. Security is not required over HTTP.
2. Error and response message handling is limited in this version.
3. Although it is possible to package this service as a traditional WAR file for deployment to an external application server, a simpler standalone approach has been used for testing.
4. Repository persistence is in-memory, so when the standalone application stops its content is wiped.

# Development notes
Tests are available under
```sh
src/test/java/org/lab/
```
From the source folder of the project you can build a JAR file typing this command
```sh
./mvnw clean package
```
Then you can run the JAR file
```sh
java -jar target/offer-rest-api-0.1.0.jar
```
This is the standalone class to use for running the application
```sh
src/main/java/org/lab/Application.java
```
The controller Java class for the RESTful API is the following
```sh
src/main/java/org/lab/OfferController.java
```

OfferUtils Java clas includes methods to validate an offer, check if its duration is valid and determine if an offer is expired.
```java
/** An offer is  checked against its constraint annotations and its period of validity.
* @param offer the offer to validate.
* @return if the offer is valid.
*/
```
```java
public static boolean isValid(Offer offer)
/**
 * @param offer the offer.
 * @return true if the period of validity (end - start) is non negative.
 */
public static boolean isPeriodValid(Offer offer)
```
```java
/**
 * @param offer the offer.
 * @return true if the offer is expired.
 */
public static boolean isExpired(Offer offer)
```
