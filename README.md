# Smart Campus REST API

JAX-RS coursework starter project for managing campus rooms, sensors, and sensor readings using an in-memory data store.

## Design Overview

- Versioned base path: `/api/v1`
- Resources:
  - `/api/v1` discovery metadata
  - `/api/v1/rooms` room management
  - `/api/v1/sensors` sensor management
  - `/api/v1/sensors/{sensorId}/readings` historical readings
- Persistence is in-memory only using Java collections
- Embedded runtime uses Jersey + Grizzly
- Request/response logging is handled with JAX-RS filters
- Custom exception mappers return structured JSON errors

## Build

```bash
mvn clean package
```

## Run

```bash
mvn exec:java
```

The API starts on `http://localhost:8080/api/v1`.

## Sample curl Commands

```bash
curl http://localhost:8080/api/v1
```

```bash
curl http://localhost:8080/api/v1/rooms
```

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":40}'
```

```bash
curl http://localhost:8080/api/v1/sensors
```

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":21.4,"roomId":"LIB-301"}'
```

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":22.1}'
```
