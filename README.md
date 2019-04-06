### Simple Stocks API

This is an example application that provides the means of managing simple stocks and trades.

This is offered as a simple web service that provides the following operations:
- Record a trade
- Record a stock
- Get stock details
- Calculate stock dividend
- Calculate stock P/E ratio
- Calculate stock price based on trades recorded in the past X minutes (configurable)
- Calculate the GBCE All Share Index using geometric mean of prices for all stocks

### Requirements
- Java 8
- Maven 3

### Design
The application is structured in layers, namely:

A *controller* is responsible for handling the requests and communicating with the layer service for providing a response. Here the internal domain is converted to DTOs to eliminate the coupling between the API and the internal domain.

A *service* is responsible for executing the business logic, here we know how to compute stock prices and GBCE indexes.

A *domain object* represents the model (Stock and Trade)

A *repository* abstracts the underlying datastore. For the sake of the example, the underlying datastore is a local hash set.

### Code structure
The *application* contains the main application source code and unit tests.

The *integration-tests* module contains the end-to-end tests made at web service level.

### Build

    project$ mvn clean install

### Running

	project$ cd application
    project$ mvn spring-boot:run

For validation purposes you can now access localhost:8080 in the corresponding endpoints in order to test the service. The following endpoints are available:

    POST /stock - Create new stock
    POST /trade - Create new trade

    GET /stocks?symbol=GIN - Get stock details for stock with symbol GIN
    GET /stocks/dividend?symbol=GIN - Calculate dividend yield for stock with symbol GIN
    GET /stocks/peratio?symbol=GIN - Calculate P/E ratio for stock with symbol GIN
    GET /stocks/price?symbol=GIN - Calculate the stock price for stock with symbol GIN
    GET /stocks/gbceasi - Calculate GBCE all shares index

The following CURL examples might come in handy while validating the application

    curl -H "Content-Type: application/json" -X POST -d '{"symbol":"GIN","type": "COMMON","lastDividend": "8","fixedDividend": "0.02","parValue": "100","tickerPrice": "1.1"}' http://localhost:8080/stock
    curl -H "Content-Type: application/json" -X POST -d '{"stockSymbol": "GIN","price": "10.6","quantity": "3","indicator": "BUY"}' http://localhost:8080/trade

    curl -H "Content-Type: application/json" http://localhost:8080/stocks?symbol=GIN
    curl -H "Content-Type: application/json" http://localhost:8080/stocks/dividend?symbol=GIN
    curl -H "Content-Type: application/json" http://localhost:8080/stocks/peratio?symbol=GIN
    curl -H "Content-Type: application/json" http://localhost:8080/stocks/price?symbol=GIN
    curl -H "Content-Type: application/json" http://localhost:8080/stocks/gbceasi

### Integration Tests
Integration tests run under a specific maven profile so it is possible to run them in a separate pipeline stage.

    project$ mvn clean install -Pintegration-tests

