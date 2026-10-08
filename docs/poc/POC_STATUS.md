# API Gateway Proof of Concept (POC) Status

## 1. Overview

This document records the evaluation status, routing architectures, and diagnostic baselines for the two API Gateway Proof of Concept (POC) implementations integrated with MainetService:
1. **Apache APISIX**
2. **WSO2 API Manager 4.x**

---

## 2. POC Status Matrix

| Component | Architecture Role | Test Type | Status | Evidence / Notes |
| :--- | :--- | :--- | :--- | :--- |
| **MainetService Direct Baseline** | Backend REST Application (WildFly 10) | HTTP POST `/doGetPayeeNames` (`{"orgId":1}`) | **PASS** | Returns HTTP 404 (`"No Record found for provided input"`). Controller execution confirmed via `MainetService.log`. |
| **Apache APISIX Route** | Ingress API Gateway | Dynamic Upstream Routing | **PASS** | Verified routing to WildFly `http://host.docker.internal:8080/MainetService/rest/*`. Header `X-Request-Id` injected and logged. |
| **WSO2 API Gateway (Admin)** | Enterprise Lifecycle Gateway | Direct Invocation via Publisher / DevPortal | **PASS** | Validated direct invocation, token issuance, and proxying to backend WildFly listener. |
| **WSO2 Gateway (HTTP 400 Diagnosis)**| Gateway Request Proxying | Endpoint Payload Validation | **PASS** | Diagnosed: Request payload `{}` triggers Spring controller 400 validation error; payload `{"orgId":1}` passes to WildFly controller returning matching HTTP 404 application response. |
| **Credential Sanitization** | Security Gate | Secret Scan & Template Setup | **PASS** | All admin keys, encryption keys, and database passwords sanitized. Safe `.example` templates provided. |

---

## 3. Apache APISIX POC Details

- **Location**: `apisix-poc/`
- **Components**:
  - `docker-compose.yml`: APISIX 3.8.0 standalone deployment with etcd.
  - `config/config.yaml.example`: Declarative APISIX gateway configuration.
  - `scripts/init-routes.ps1` & `scripts/init-routes.sh`: Automated route provisioning scripts.
- **Route Definition**:
  - Ingress: `POST /MainetService/rest/*`
  - Upstream: `http://host.docker.internal:8080/MainetService/rest/*`
  - Plugins: `request-id` (adds UUID header for end-to-end tracing).

---

## 4. WSO2 API Manager POC Details

- **Location**: `wso2-apim-poc/`
- **Components**:
  - `docker-compose.yml`: Single-node WSO2 APIM 4.2.0 container.
  - `deployment.toml.example`: Sanitized WSO2 deployment configuration template.
  - `scripts/`: Diagnostic and verification scripts.
- **API Lifecycle Flow**:
  - Publisher: API defined with context `/mainet-service/1.0.0` pointing to production endpoint `http://host.docker.internal:8080/MainetService/rest`.
  - DevPortal: Application created, subscribed to API, OAuth 2.0 keys generated.
  - Gateway: Inbound bearer tokens validated, mutual TLS/HTTP proxied to backend.
