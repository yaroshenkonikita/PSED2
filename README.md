# PSED2: Currency RPC + Pact Contracts

Repository contains two Spring Boot services:

1. `currency-rate-provider`
   JSON-RPC provider returning rates with request parameters (pair + time).
2. `rate-printer`
   Consumer polling provider every 5 seconds and printing result to console.

## Stack

- Java 17+
- Maven 3.9+
- Zookeeper (service discovery)
- Pact Broker (contract storage)

## Start infrastructure

From repository root:

```powershell
docker compose up -d
```

Ports:

- Zookeeper: `2181`
- Pact Broker: `9292`
- Pact Broker Postgres: `5432`

## API Paths

Provider endpoint:

- Versioned API: `POST /api/v1/rpc`

Main JSON-RPC method:

- `getRate` with params:
  - `pair` (for example `USD/RUB`)
  - `at` (ISO-8601 timestamp, for example `2026-03-10T08:00:00Z`)


## Run services

Terminal 1:

```powershell
cd currency-rate-provider
mvn spring-boot:run
```

Terminal 2:

```powershell
cd rate-printer
mvn spring-boot:run
```

## Contract workflow (Pact)

### 1. Consumer generates and publishes contract

```powershell
cd rate-printer
mvn clean verify
```

What happens:

- `RateProviderPactTest` generates pact file into `target/pacts`
- Maven Pact plugin publishes pact to broker (`http://localhost:9292`)
- Consumer version for pact publish is unique per build (`<project.version>-<timestamp>`)

### 2. Provider verifies contracts from broker during build

```powershell
cd currency-rate-provider
mvn clean verify
```

What happens:

- `RateProviderPactVerificationTest` pulls contracts from Pact Broker
- Provider API is started on random port
- All interactions are verified against provider implementation

If verification fails, build fails.

## Useful flags

Skip pact publish on consumer side:

```powershell
mvn clean verify -DskipPactPublish=true
```

Override Pact Broker location:

```powershell
mvn clean verify -Dpact.broker.url=http://localhost:9292
mvn clean verify -Dpactbroker.host=localhost -Dpactbroker.port=9292 -Dpactbroker.scheme=http
```
