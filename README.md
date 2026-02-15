# Homework: Service Registry with Zookeeper

This repository contains two Spring Boot services:

1. `currency-rate-provider`  
   JSON-RPC server that returns current `USD/RUB` rate with small random changes.
2. `rate-printer`  
   Consumer that calls provider every 5 seconds and prints rate to console.

## Requirements

- Java 17+
- Maven 3.9+
- Apache Zookeeper (for service registry)

## Start Zookeeper

Example with Docker:

```powershell
docker run --name psed2-zk -p 2181:2181 -d zookeeper:3.9
```

## Run

Open terminals in this repository root.

Terminal 1 (provider instance 1):

```powershell
cd currency-rate-provider
mvn spring-boot:run
```

Terminal 2 (provider instance 2, optional for load balancing):

```powershell
cd currency-rate-provider
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Terminal 3 (consumer):

```powershell
cd rate-printer
mvn spring-boot:run
```

What happens:

- each `currency-rate-provider` instance auto-registers in Zookeeper under service name `currency-rate-provider`
- `rate-printer` resolves instances from Zookeeper and balances requests between them via Spring Cloud LoadBalancer

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
