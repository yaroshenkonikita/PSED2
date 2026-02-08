# Homework 1: Two Java Services

This repository contains two Spring Boot services:

1. `currency-rate-provider`  
   JSON-RPC server that returns current `USD/RUB` rate with small random changes.
2. `rate-printer`  
   Client that calls provider every 5 seconds and prints rate to console.

## Requirements

- Java 17+
- Maven 3.9+

## Run

Open two terminals in this repository root.

Terminal 1 (provider):

```powershell
cd currency-rate-provider
mvn spring-boot:run
```

Terminal 2 (printer):

```powershell
cd rate-printer
mvn spring-boot:run
```

## RPC API (provider)

`POST http://localhost:8080/rpc`

Request example:

```json
{
  "jsonrpc": "2.0",
  "method": "getUsdRubRate",
  "params": null,
  "id": 1
}
```

Response example:

```json
{
  "jsonrpc": "2.0",
  "result": {
    "pair": "USD/RUB",
    "rate": 89.7341,
    "at": "2026-02-08T15:59:02.892518200Z"
  },
  "error": null,
  "id": 1
}
```
