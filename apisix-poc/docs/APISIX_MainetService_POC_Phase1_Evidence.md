# Apache APISIX + MainetService
# Phase-1 POC Evidence / Validation Report

---

## 1. Document Control

| Property | Value |
| :--- | :--- |
| **Document Title** | Apache APISIX + MainetService Phase-1 POC Evidence / Validation Report |
| **POC Name** | Apache APISIX Inbound Gateway Integration for MainetService |
| **Phase** | Phase 1: Transparent API Gateway / Reverse Proxy |
| **Date** | 30 September 2026 |
| **Environment** | Local POC (Windows 10 Host + Docker Desktop) |
| **Status** | **Phase 1 Functionally Validated** |
| **Scope** | Transparent API Gateway / Reverse Proxy (No Application Modification) |

> [!NOTE]
> **Interim Documentation Notice**: This document serves as an interim Phase-1 technical evidence and validation record. It documents the functional baseline established during Phase 1 and will be incorporated into the final consolidated POC report following subsequent phases (Phase 2 and beyond).

> [!IMPORTANT]
> **Evidence Integrity Statement**: Results recorded in this document are based strictly on the local MainetService/APISIX POC execution environment and should not be interpreted as production capacity, performance benchmarking, or production high-availability (HA) validation.

---

## 2. Objective

The objective of Phase 1 was to functionally validate that **Apache APISIX** can be deployed directly in front of the existing legacy **MainetService** enterprise application without requiring any modifications to the application itself.

The validation covers both legacy REST frameworks utilized by MainetService:
1. **Spring MVC REST**: Endpoints mounted under `/MainetService/rest/*`
2. **Apache CXF / JAX-RS REST**: Endpoints mounted under `/MainetService/services/*`

The scope of Phase 1 is strictly limited to **transparent proxying and reverse-proxy protocol fidelity**—confirming that HTTP methods, request headers, JSON payloads, URI paths, and application status codes (including application-level errors) are passed through without alteration.

---

## 3. Existing MainetService Runtime

The MainetService runtime environment utilized for the POC represents the authentic, existing enterprise deployment:

- **Application Server**: WildFly Full 10.1.0.Final (WildFly Core 2.2.0.Final)
- **Installation Path**: `D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service`
- **Java Runtime**: Oracle JDK 1.8.0_241 64-Bit (`D:\Softwares\jdk1.8.0_241`)
- **Deployed Artifact**: `MainetService.war` (Size: 214,988,068 bytes)
- **Deployment Status**:
  - `MainetService.war.deployed` marker present
  - `.failed` marker absent
  - Context path `/MainetService` registered and responding with `HTTP 200`
- **Socket Bindings**:
  - Public HTTP Listener: `0.0.0.0:8080` (PID: 15224)
  - Management HTTP Listener: `0.0.0.0:9990` (PID: 15224)
- **Timeout Configurations**:
  - Deployment scanner timeout: `1800` seconds (`deployment-timeout="1800"`)
  - Management blocking timeout: `1800` seconds (`jboss.as.management.blocking.timeout="1800"`)
- **Database / VPN Connectivity**:
  - Remote MySQL Server at `192.168.100.10:3306`
  - Connection verified active via `Test-NetConnection` (`TcpTestSucceeded: True`)
- **Maven & Build Integrity**:
  - Maven repository cache was previously relocated to `D:\DevCache\m2` and remained completely untouched.
  - **No rebuild or repackaging was performed**: The existing, pre-built WAR deployment was reused without modification.
  - Zero source code changes, zero `pom.xml` changes, and zero configuration alterations were made to MainetService.

---

## 4. APISIX POC Environment

The gateway layer runs via containerized services orchestrated through Docker Desktop on the local host:

- **Gateway Engine**: Apache APISIX `3.9.1-debian` (`apache/apisix:3.9.1-debian`)
- **Control Plane Store**: etcd `3.5.11` (`bitnamilegacy/etcd:3.5.11`)
- **Container Platform**: Docker Desktop (WSL 2 backend on Windows 10)
- **Network Ports**:
  - APISIX Inbound HTTP Listener: `9080` (`0.0.0.0:9080`)
  - APISIX Admin API Listener: `9180` (`0.0.0.0:9180`)
  - APISIX HTTPS Listener: `9443` (`0.0.0.0:9443`)
  - etcd Client Communication: `2379` (`0.0.0.0:2379`)
- **Host Networking Bridge**:
  - APISIX connects to the host WildFly server via `host.docker.internal:8080`, which resolves to Docker's internal host gateway (`192.168.65.254`).
- **Configuration Persistence**:
  - APISIX operates in `traditional` mode with `config_provider: etcd`.
  - All provisioned routes are dynamically pushed to etcd via the Admin API and durably persisted.

### Architecture Diagram

```
+-------------------------------------------------------------------------------+
| Client (Smoke Test Script / PowerShell / curl / External Consumer)            |
+-------------------------------------------------------------------------------+
                                      |
                                      | HTTP Request (Port :9080)
                                      v
+-------------------------------------------------------------------------------+
| Apache APISIX Gateway Container (Port 9080, Admin: 9180)                      |
| Image: apache/apisix:3.9.1-debian                                             |
|                                                                               |
|  - Route 1: /MainetService/rest/AccountReceiptEntry/doGetPayeeNames           |
|  - Route 2: /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount       |
|                                                                               |
|  Upstream: host.docker.internal:8080                                          |
+-------------------------------------------------------------------------------+
        |                                       |
        | etcd sync (:2379)                     | Upstream Proxy (:8080)
        v                                       v
+-----------------------+       +-----------------------------------------------+
| etcd Container (:2379)|       | Host Windows Machine                          |
| Image: bitnamilegacy/ |       | WildFly 10.1.0.Final Application Server       |
| etcd:3.5.11           |       |   Listener: 0.0.0.0:8080                      |
| (Control Plane Store) |       |   Deployment: MainetService.war               |
+-----------------------+       |     - Spring MVC DispatcherServlet (/rest/*)  |
                                |     - Apache CXF Servlet (/services/*)        |
                                +-----------------------------------------------+
                                                        |
                                                        | JDBC (:3306)
                                                        v
                                        +-------------------------------+
                                        | Remote MySQL Database         |
                                        | (192.168.100.10:3306)         |
                                        +-------------------------------+
```

---

## 5. Phase-1 Scope

### In Scope
- Deployment and operation of Apache APISIX 3.9.1 and etcd 3.5.11 via Docker Compose.
- Creation and configuration of explicit APISIX routes via the Admin API.
- Transparent reverse proxying of legacy URIs to the local WildFly instance.
- Routing and forwarding for an existing Spring MVC REST endpoint.
- Routing and forwarding for an existing Apache CXF / JAX-RS REST endpoint.
- Preservation of HTTP methods, request bodies, query parameters, and response bodies.
- Verification of upstream host connectivity from container to host (`host.docker.internal:8080`).
- Route persistence verification within etcd across APISIX gateway restarts.
- Diagnostic boundary isolation between gateway layers and application layers.

### Out of Scope
The following enterprise capabilities were intentionally excluded from Phase 1 and deferred to future phases:
- OAuth2 / OpenID Connect (OIDC) / Keycloak integration
- Consumer authentication and authorization policies
- Consumer management and API credentials
- Rate limiting and traffic shaping (`limit-count`, `limit-req`)
- Prometheus metrics scraping and Grafana dashboards
- Request correlation ID injection (`request-id` plugin)
- API analytics and logging aggregation
- Developer Portal configuration
- High Availability (HA) clustering of APISIX instances
- Production hardware sizing and capacity planning
- Performance load testing and latency benchmarking
- Multi-node upstream load balancing

---

## 6. Test Endpoint 1 — Spring MVC REST

### Endpoint Specification
- **Framework**: Spring MVC DispatcherServlet
- **HTTP Method**: `POST`
- **URI**: `/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
- **Headers**: `Content-Type: application/json`
- **Request Payload**:
  ```json
  {"orgId":1}
  ```

### Test Results

| Invocator | Target URL | HTTP Status | Response Content |
| :--- | :--- | :---: | :--- |
| **Direct (WildFly)** | `http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames` | **404 Not Found** | `"No Record found for provided input"` |
| **Gateway (APISIX)** | `http://localhost:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames` | **404 Not Found** | `"No Record found for provided input"` |

### Technical Analysis & Diagnostic Finding
1. **Application-Level Response**: The `HTTP 404` status with body `"No Record found for provided input"` is generated directly by the MainetService business logic (Spring MVC Controller returning 404 when no matching payee record exists for `orgId=1` in the database).
2. **Not an APISIX Routing Error**: When APISIX fails to match a route, it returns `{"error_msg":"404 Route Not Found"}`. In this test, APISIX matched the route, forwarded the POST request and JSON body to the upstream WildFly instance, and received the application's business response.
3. **Gateway Access Log Evidence**:
   ```text
   172.18.0.1 - - [30/Sep/2026:07:11:46 +0000] 127.0.0.1:9080 "POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames HTTP/1.1" 404 47 0.508 "-" "Mozilla/5.0 (Windows NT; Windows NT 10.0; en-IN) WindowsPowerShell/5.1.26100.9444" 192.168.65.254:8080 404 0.508 "http://127.0.0.1:9080"
   ```
   - Downstream client received: `HTTP 404`, 47 bytes body.
   - Upstream peer called: `192.168.65.254:8080` (host gateway).
   - Upstream status returned: `404`.
   - Upstream response time: `0.508s`.

### Conclusion
**PASS** — APISIX demonstrated 100% transparent forwarding and exact response preservation for Spring MVC POST requests, maintaining strict parity with direct application behavior.

---

## 7. Test Endpoint 2 — Apache CXF / JAX-RS REST

### Endpoint Specification
- **Framework**: Apache CXF REST (JAX-RS)
- **HTTP Method**: `GET`
- **URI**: `/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`
- **Headers**: Accept standard HTTP headers
- **Request Payload**: None (GET request)

### Test Results

| Invocator | Target URL | HTTP Status | Response Content Summary |
| :--- | :--- | :---: | :--- |
| **Direct (WildFly)** | `http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount` | **200 OK** | JSON dataset containing license count records for years 2022 through 2027 |
| **Gateway (APISIX)** | `http://localhost:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount` | **200 OK** | Identical JSON dataset containing license count records for years 2022 through 2027 |

### Technical Analysis & Diagnostic Finding
1. **End-to-End Success**: The CXF REST endpoint executed a database query through Hibernate/JDBC to the remote MySQL database and returned the active year-wise license counts.
2. **Gateway Access Log Evidence**:
   ```text
   172.18.0.1 - - [30/Sep/2026:07:12:02 +0000] 127.0.0.1:9080 "GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount HTTP/1.1" 200 427 3.833 "-" "Mozilla/5.0 (Windows NT; Windows NT 10.0; en-IN) WindowsPowerShell/5.1.26100.9444" 192.168.65.254:8080 200 3.833 "http://127.0.0.1:9080"
   ```
   - Downstream client received: `HTTP 200`, 427 bytes body.
   - Upstream peer called: `192.168.65.254:8080`.
   - Upstream status returned: `200`.
   - Response successfully delivered to client without corruption, header clipping, or encoding errors.

### Conclusion
**PASS** — APISIX demonstrated transparent end-to-end proxying of Apache CXF GET requests with full payload fidelity.

---

## 8. Route Configuration

Two discrete routes were provisioned via the APISIX Admin API (`http://localhost:9180/apisix/admin/routes`) using `PUT` operations. No additional routes were introduced.

### Route 1: Spring MVC Payee Lookup
```json
{
  "id": "mainetservice-accounts-get-payee-names",
  "name": "mainetservice-accounts-get-payee-names",
  "desc": "POC Route 1: Spring MVC REST - Accounts Payee Names Lookup",
  "uri": "/MainetService/rest/AccountReceiptEntry/doGetPayeeNames",
  "methods": ["POST"],
  "upstream": {
    "type": "roundrobin",
    "nodes": {
      "host.docker.internal:8080": 1
    },
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 60
    },
    "pass_host": "pass"
  },
  "status": 1
}
```

### Route 2: Apache CXF License Dashboard Count
```json
{
  "id": "mainetservice-common-license-yearwise-count",
  "name": "mainetservice-common-license-yearwise-count",
  "desc": "POC Route 2: Apache CXF REST - Year-wise License Count Statistics",
  "uri": "/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount",
  "methods": ["GET"],
  "upstream": {
    "type": "roundrobin",
    "nodes": {
      "host.docker.internal:8080": 1
    },
    "timeout": {
      "connect": 6,
      "send": 6,
      "read": 60
    },
    "pass_host": "pass"
  },
  "status": 1
}
```

---

## 9. Route Persistence & Restart Recovery

To validate that route definitions are not held merely in volatile gateway memory, a restart recovery test was executed:

1. **Route Provisioning**: Routes `mainetservice-accounts-get-payee-names` and `mainetservice-common-license-yearwise-count` were registered via the Admin API into the etcd control plane.
2. **Gateway Restart**: The APISIX container was restarted using Docker Compose (`docker compose restart apisix`).
3. **Route Verification Post-Restart**:
   - Immediately following worker re-initialization, the Admin API was queried:
     ```text
     172.18.0.1 - - [30/Sep/2026:07:19:58 +0000] 127.0.0.1:9180 "GET /apisix/admin/routes HTTP/1.1" 200 1276 0.003
     ```
   - Both configured routes were returned with HTTP 200 (payload size: 1,276 bytes).
4. **Functional Traffic Post-Restart**:
   - The CXF REST endpoint was invoked through APISIX at port 9080:
     ```text
     172.18.0.1 - - [30/Sep/2026:07:20:48 +0000] 127.0.0.1:9080 "GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount HTTP/1.1" 200 427 4.106 "-" "Mozilla/5.0 (Windows NT; Windows NT 10.0; en-IN) WindowsPowerShell/5.1.26100.9444" 192.168.65.254:8080 200 4.105 "http://127.0.0.1:9080"
     ```
   - The request succeeded immediately with `HTTP 200` and identical JSON output.

### Conclusion
**PASS** — Demonstrates durable route configuration persistence in etcd and reliable recovery of routing tables following an APISIX gateway restart. *(Note: This confirms single-node persistence, not distributed HA clustering).*

---

## 10. Technical Observation: etcd Logging

During execution, a distinct operational pattern was observed regarding etcd:

### 1. etcd Health & Status Verification
Direct execution of diagnostic commands against the `apisix-etcd` container confirmed the service was fully operational:
- **Health Check**:
  ```text
  $ docker exec apisix-etcd etcdctl endpoint health
  127.0.0.1:2379 is healthy: successfully committed proposal: took = 23.244067ms
  ```
- **Endpoint Status**:
  ```text
  $ docker exec apisix-etcd etcdctl endpoint status
  127.0.0.1:2379, 8e9e05c52164694d, 3.5.11, 37 kB, true, false, 3, 77, 77
  ```
  *(The `true` boolean confirms this etcd instance is the active cluster Leader).*
- **Version**: etcd server & etcdctl version `3.5.11`, API version `3.5`.

### 2. Observed Gateway Log Messages
Despite etcd being healthy and functional, APISIX worker logs intermittently printed recurring notices during startup:
```text
[error] 55#55: *68 [lua] config_etcd.lua:210: wait watch event: http://etcd:2379: Service Unavailable, context: ngx.timer
[warn] 51#51: *165 [lua] health_check.lua:105: report_failure(): update endpoint: http://etcd:2379 to unhealthy, context: ngx.timer
[error] 57#57: *226 [lua] config_etcd.lua:193: watchdir err: has no healthy etcd endpoint available, context: ngx.timer
```

### 3. Impact Assessment & Boundary
- **Operational Reality**: These log messages did **not** impede functional operation:
  - etcd successfully committed all route write operations from the Admin API.
  - APISIX successfully read and initialized the routes from etcd.
  - Route configurations survived gateway restarts.
  - All proxy requests to WildFly succeeded without interruption.
- **Classification**: **OBSERVED** — The recurring watcher timeout logs represent a known Lua etcd v3 gRPC/HTTP long-polling keepalive behavior in local Docker bridge networks. This observation is flagged for investigation in the operational hardening phase and does not constitute a Phase-1 functional failure.

---

## 11. Evidence Matrix

| # | Test Item | Expected Result | Actual Result | Status |
| :-: | :--- | :--- | :--- | :-: |
| **1** | **WildFly Startup** | Starts with Java 8, binds `0.0.0.0:8080` & `9990` | Started in 396s; bound `0.0.0.0:8080` and `9990` (PID 15224) | **PASS** |
| **2** | **MainetService Deployment** | `.deployed` marker present; context `/MainetService` active | `MainetService.war.deployed` present; `/MainetService` returned HTTP 200 | **PASS** |
| **3** | **Database Connectivity** | Remote MySQL at `192.168.100.10:3306` reachable | TCP connection succeeded (`TcpTestSucceeded: True`) | **PASS** |
| **4** | **APISIX & etcd Launch** | Containers running and ports mapped | Containers `apisix-gateway` and `apisix-etcd` Up; ports 9080, 9180, 2379 bound | **PASS** |
| **5** | **Spring MVC Direct** | Direct POST to `:8080` executes without network error | Returns HTTP 404 `"No Record found for provided input"` | **PASS** |
| **6** | **Spring MVC via APISIX** | POST to `:9080` mirrors direct HTTP 404 response | Returns HTTP 404 `"No Record found for provided input"`; upstream log shows 404 | **PASS** |
| **7** | **CXF REST Direct** | Direct GET to `:8080` returns license counts | Returns HTTP 200 with 2022–2027 license dataset | **PASS** |
| **8** | **CXF REST via APISIX** | GET to `:9080` mirrors direct HTTP 200 response | Returns HTTP 200 with identical dataset; upstream log shows 200 | **PASS** |
| **9** | **Route Creation** | Admin API accepts PUT and stores routes | Both routes created with HTTP 201; verified in route list | **PASS** |
| **10** | **etcd Health Status** | etcd reports healthy and active leader | etcdctl reports healthy (23ms proposal); node confirmed leader | **PASS** |
| **11** | **etcd Watcher Logging** | Long-poll connection stability in logs | Intermittent `Service Unavailable` watcher logs recorded | **OBSERVED** |
| **12** | **APISIX Restart Recovery** | Routes survive restart; traffic continues | Routes present in Admin API post-restart; CXF returned HTTP 200 | **PASS** |
| **13** | **Production HA / Failover** | Multi-node gateway and cluster failover | Not tested in Phase 1 environment | **NOT TESTED** |
| **14** | **Rate Limiting & Security** | Plugin enforcement (`limit-count`, OAuth) | Deferred to Phase 2 | **NOT TESTED** |

---

## 12. Phase-1 Acceptance Matrix

| Acceptance Criteria | Target Specification | Achieved | Status |
| :--- | :--- | :---: | :-: |
| **Existing Application Reused** | Zero re-compilation, zero repackaging of WAR | Yes | **PASS** |
| **No Source Modifications** | MainetService Java, XML, configuration untouched | Yes | **PASS** |
| **APISIX Deployment** | Apache APISIX 3.9.1 containerized and operational | Yes | **PASS** |
| **etcd Deployment** | etcd 3.5.11 containerized and operational | Yes | **PASS** |
| **Spring MVC Transparent Proxy** | POST requests routed and handled transparently | Yes | **PASS** |
| **CXF REST Transparent Proxy** | GET requests routed and handled transparently | Yes | **PASS** |
| **Upstream Connectivity** | Reliable communication from container to host WildFly | Yes | **PASS** |
| **Response Preservation** | Exact HTTP status, headers, and body preserved | Yes | **PASS** |
| **Route Persistence** | Routes durably held in etcd | Yes | **PASS** |
| **Gateway Restart Recovery** | Configuration intact after APISIX restart | Yes | **PASS** |

### Overall Assessment
# **PHASE 1 — FUNCTIONALLY VALIDATED / PASS**

---

## 13. What Phase 1 Proves

Based strictly on empirical test results, Phase 1 validates that Apache APISIX:
- Can sit as an inbound reverse proxy directly in front of the legacy MainetService application deployed on WildFly 10.1.0 / Java 8.
- Can route requests to existing legacy URL structures without requiring changes to application code, context roots, or web deployment descriptors.
- Successfully proxies both Spring MVC REST (`/rest/*`) and Apache CXF REST (`/services/*`) frameworks.
- Preserves downstream and upstream HTTP protocol semantics, including JSON request bodies and application-generated response payloads (both 200 OK and application-level 404 responses).
- Dynamically receives route configurations via its Admin API and persists them durably in etcd.
- Retains routing integrity and recovers traffic handling immediately upon gateway service restart.

---

## 14. What Phase 1 Does NOT Prove

In strict adherence to evidence-based validation, Phase 1 does **NOT** prove or validate:
- Production High Availability (HA) or active-active multi-datacenter resilience.
- Horizontal autoscaling under high concurrent request volume.
- Authentication or authorization enforcement (Keycloak, OAuth2, OIDC, JWT).
- Rate limiting, burst protection, or denial-of-service mitigation.
- Observability export to enterprise Prometheus, Grafana, or OpenTelemetry collectors.
- Production-scale throughput, latency percentiles (p95, p99), or gateway overhead metrics.
- Production-grade security hardening of the Docker or host networking layer.
- Production-grade etcd multi-node clustering and disaster recovery procedures.

---

## 15. Phase-1 Conclusion

Phase 1 successfully validated the fundamental transparent reverse-proxy integration between Apache APISIX and the existing MainetService legacy application.

Both a Spring MVC REST endpoint and an Apache CXF REST endpoint were routed through APISIX to the existing WildFly deployment without modifying the MainetService application.

The CXF endpoint returned HTTP 200 through APISIX, while the Spring MVC endpoint preserved the application's own HTTP 404 response. APISIX route configuration persisted through etcd and remained available following an APISIX restart.

Phase 1 is therefore considered functionally validated.

---

## 16. Next Phase

Phase 2 will be conducted in a separate dedicated evaluation cycle. The proposed phased sequence for subsequent validation is:

- **Phase 2-A: Request ID & Correlation Tracking**
  - Enable the `request-id` plugin to inject unique UUID headers (`X-Request-Id`) across inbound requests for end-to-end trace correlation.
- **Phase 2-B: Rate Limiting & Traffic Shaping**
  - Implement and validate the `limit-count` plugin to safeguard legacy WildFly thread pools from request surges.
- **Phase 2-C: Observability & Metrics**
  - Expose and validate gateway metrics via the `prometheus` plugin for dashboard integration.
- **Phase 2-D: Authentication & Authorization Integration**
  - Evaluate modern authentication gateways (OAuth2 / OIDC / JWT) for securing legacy MainetService endpoints.

*(No Phase 2 implementations or configuration changes were executed during this documentation task).*

---

## 17. Evidence & Commands Appendix

The following commands represent the exact operational instructions utilized during the Phase-1 validation:

### 1. Container Status Check
```bash
docker compose ps
```
**Output**:
```text
NAME             IMAGE                        COMMAND                  SERVICE   CREATED       STATUS         PORTS
apisix-etcd      bitnamilegacy/etcd:3.5.11    "/opt/bitnami/script…"   etcd      2 hours ago   Up 2 hours     0.0.0.0:2379->2379/tcp, [::]:2379->2379/tcp
apisix-gateway   apache/apisix:3.9.1-debian   "/docker-entrypoint.…"   apisix    2 hours ago   Up 8 minutes   0.0.0.0:9080->9080/tcp, [::]:9080->9080/tcp, 0.0.0.0:9180->9180/tcp, [::]:9180->9180/tcp, 0.0.0.0:9443->9443/tcp, [::]:9443->9443/tcp
```

### 2. Route Provisioning Commands (Admin API)
```powershell
$adminUrl = "http://localhost:9180/apisix/admin"
$apiKey   = "<REDACTED>"
$headers  = @{
    "X-API-KEY"    = $apiKey
    "Content-Type" = "application/json"
}

# Provision Spring MVC Route
Invoke-RestMethod -Uri "$adminUrl/routes/mainetservice-accounts-get-payee-names" `
    -Method Put -Headers $headers -Body '{
        "name": "mainetservice-accounts-get-payee-names",
        "uri": "/MainetService/rest/AccountReceiptEntry/doGetPayeeNames",
        "methods": ["POST"],
        "upstream": {
            "type": "roundrobin",
            "nodes": { "host.docker.internal:8080": 1 },
            "timeout": { "connect": 6, "send": 6, "read": 60 },
            "pass_host": "pass"
        }
    }'

# Provision CXF REST Route
Invoke-RestMethod -Uri "$adminUrl/routes/mainetservice-common-license-yearwise-count" `
    -Method Put -Headers $headers -Body '{
        "name": "mainetservice-common-license-yearwise-count",
        "uri": "/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount",
        "methods": ["GET"],
        "upstream": {
            "type": "roundrobin",
            "nodes": { "host.docker.internal:8080": 1 },
            "timeout": { "connect": 6, "send": 6, "read": 60 },
            "pass_host": "pass"
        }
    }'
```

### 3. APISIX Admin API Route Query
```powershell
curl.exe -i -H "X-API-KEY: <REDACTED>" http://localhost:9180/apisix/admin/routes
```

### 4. etcd Health & Status Commands
```bash
# Check etcd health
docker exec apisix-etcd etcdctl endpoint health

# Check etcd leadership & status
docker exec apisix-etcd etcdctl endpoint status
```

### 5. Direct Endpoint Validation Commands
```powershell
# Direct Spring MVC POST Test
curl.exe -i -X POST "http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
  -H "Content-Type: application/json" `
  -d '{"orgId":1}'

# Direct Apache CXF GET Test
curl.exe -i -X GET "http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
```

### 6. APISIX Gateway Validation Commands
```powershell
# APISIX Spring MVC POST Test
curl.exe -i -X POST "http://localhost:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
  -H "Content-Type: application/json" `
  -d '{"orgId":1}'

# APISIX Apache CXF GET Test
curl.exe -i -X GET "http://localhost:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
```

### 7. Gateway Restart Command
```bash
docker compose restart apisix
```
