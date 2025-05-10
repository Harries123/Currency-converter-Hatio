# Currency Converter

A Spring Boot application integrating with a public API to provide real-time currency exchange rates and conversions.

---

## Technologies Used:
- Java 21  
- Spring Boot 3.2.3  
- Maven  
- REST API  
- JUnit 5 (unit testing)  
- WebClient (non-blocking API calls)  
- Springdoc OpenAPI (API documentation)  
- Lombok (reducing boilerplate code)  

---

## Project Structure:
src/main/java/com/currencyconverter/
- client/ExchangeRateClient.java  
- config/AppConfig.java  
- controller/CurrencyController.java  
- exception/ApiError.java  
- exception/ExternalApiException.java  
- exception/GlobalExceptionHandler.java  
- exception/InvalidCurrencyException.java  
- model/ConversionRequest.java  
- model/ConversionResponse.java  
- model/ExchangeRateResponse.java  
- service/CurrencyService.java  
- service/CurrencyServiceImpl.java  
- service/CurrencyConverterApplication.java  
- resources/application.properties  

src/test/java/com/currencyconverter/
- controller/CurrencyControllerTest.java  
- service/CurrencyServiceImplTest.java  

pom.xml  

---

## Setup Instructions:
1. Clone the Repository:  

git clone https://github.com/Harries123/Currency-converter-Hatio.git
cd Currency-converter-Hatio


2. Update Application Properties:  

openexchangerates.api.url=https://v6.exchangerate-api.com/v6
openexchangerates.app.id=d97ec46997c7f5f6c25b4e54
server.port=8080


3. Build and Run the Project:  

mvn clean install
mvn spring-boot:run

## API Endpoints:
Fetch Exchange Rates:  
GET http://localhost:8080/api/rates?base=USD

Convert Currency:  
POST http://localhost:8080/api/convert
Content-Type: application/json

{
"from": "USD",
"to": "EUR",
"amount": 100
}


---

## Testing:
Run all tests:  
mvn test


Run a specific test class:  
mvn -Dtest=CurrencyControllerTest test





