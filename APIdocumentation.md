# Currency Converter API Documentation

The Currency Converter API provides real-time exchange rates and currency conversion functionality. Users can fetch exchange rates for a base currency or convert amounts between currencies using the latest exchange rates.

**Base URL:**
`http://localhost:8080`

## 1. Fetch Currency Exchange Rates

**Endpoint:**
`GET /api/rates?base={baseCurrency}`

**Description:**
Fetch the latest exchange rates for the specified base currency.

**Example Request:**

GET : http://localhost:8080/api/rates?base=USD

## 2. Convert Currency

**Endpoint:**
`POST /api/convert`

**Description:**
Convert an amount from one currency to another using the latest exchange rates.

**Example Request:**


POST : http://localhost:8080/api/convert  
Content-Type: application/json

{
    "from": "USD",
    "to": "EUR",
    "amount": 100
}


