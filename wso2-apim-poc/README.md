# WSO2 API Manager 4.7.0 POC — MainetService Integration

## 1. Overview

This POC demonstrates the integration and reverse-proxying of legacy MainetService REST endpoints through **WSO2 API Manager 4.7.0**, validating OAuth2/JWT token enforcement, request dispatch, and response fidelity against WildFly 10.1.0 / Java 8.

## 2. Architecture

```text
  Client Application / curl
             |
             v (HTTPS / port 8244)
    +-------------------------------------------------+
    |               WSO2 API Gateway                  |
    |                                                 |
    |   1. OAuth2 / JWT Validation (Resident KM)     |
    |   2. Subscription & Throttling Enforcement     |
    |   3. Resource Mapping:                         |
    |      /mainetservice/accounts/v1/*              |
    |      -> http://host.docker.internal:8080/       |
    |         MainetService/rest/*                    |
    |   4. Passthrough Transport NIO Engine          |
    +-------------------------------------------------+
             |
             v (HTTP / port 8080)
    +-------------------------------------------------+
    |             WildFly 10.1.0 / Undertow            |
    |                                                 |
    |   1. MainetRequestLoggingFilter                 |
    |   2. Spring MVC DispatcherServlet               |
    |   3. AccountReceiptEntryRestController          |
    |   4. AccountReceiptEntryServiceImpl             |
    |   5. Hibernate / JDBC / Database               |
    +-------------------------------------------------+
```

## 3. Port Mappings

| Host Port | Container Port | Purpose | Protocol |
| :---: | :---: | :--- | :---: |
| **8244** | 8243 | API Gateway HTTPS Listener | HTTPS |
| **9444** | 9443 | Management Console, Publisher, DevPortal | HTTPS |

## 4. Quick Start

### 4.1 Prerequisites
- Docker Engine & Docker Compose
- Host WildFly listening on port 8080

### 4.2 Configuration Template
Copy `deployment.toml.example` to `deployment.toml` and customize the environment secrets:
```bash
cp deployment.toml.example deployment.toml
```

Key configuration elements in `deployment.toml`:
```toml
[server]
hostname = "localhost"

[transport.https.properties]
proxyPort = 9444

[[apim.jwt.issuer]]
name = "https://localhost:9443/oauth2/token"
jwks.url = "https://localhost:9443/oauth2/jwks"
```

### 4.3 Launch Container
```bash
docker compose up -d
```

### 4.4 Verify Health
- DevPortal: `https://localhost:9444/devportal`
- Publisher: `https://localhost:9444/publisher`
- Gateway: `https://localhost:8244/`

## 5. Published API Specification

- **API Name**: `MainetService Accounts`
- **Context**: `/mainetservice/accounts`
- **Version**: `v1`
- **Resource**: `POST /AccountReceiptEntry/doGetPayeeNames`
- **Upstream Production Endpoint**: `http://host.docker.internal:8080/MainetService/rest`
- **Security**: OAuth2 (Application / User JWT token)

## 6. Testing Endpoints

### 6.1 Token Generation (Client Credentials Grant)
```bash
curl -k -X POST -u "${CLIENT_ID}:${CLIENT_SECRET}" \
  -d "grant_type=client_credentials" \
  https://localhost:9444/oauth2/token
```

### 6.2 Gateway Endpoint Invocation
```bash
# Note: Ensure JSON payload is strictly formatted with quoted keys
curl -k -i -X POST \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  --data '{"orgId":1}' \
  "https://localhost:8244/mainetservice/accounts/v1/AccountReceiptEntry/doGetPayeeNames"
```

Expected Response:
- **HTTP Status**: `HTTP/1.1 404 Not Found`
- **Body**: `"No Record found for provided input"` (Application-level response from MainetService controller).
