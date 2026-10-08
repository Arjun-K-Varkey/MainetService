# Apache APISIX Proof of Concept (POC) – Phase 1: MainetService Inbound Gateway

## 1. Purpose
The purpose of this Phase 1 Proof of Concept (POC) is to validate that **Apache APISIX** can serve as an inbound API Gateway placed directly in front of the existing legacy **MainetService** multi-module enterprise Java application deployed on **WildFly 10.1.0 / JBoss**, with **zero modifications** to the legacy application source code, configuration files, build scripts, or deployment artifacts.

This POC establishes a clean baseline reverse-proxy configuration preserving exact URIs, HTTP methods, headers, and request bodies for both legacy REST stacks:
1. **Spring MVC REST**: `/MainetService/rest/*`
2. **Apache CXF REST**: `/MainetService/services/*`

---

## 2. Architecture

### Target Topology (Phase 1)
```
+-------------------------------------------------------------------------------+
| Client (Postman, curl, Frontend, External Consumers)                          |
+-------------------------------------------------------------------------------+
                                      |
                                      | HTTP POST (e.g. :9080)
                                      v
+-------------------------------------------------------------------------------+
| Apache APISIX Gateway Container (Docker: port 9080, admin: 9180)              |
| Image: apache/apisix:3.9.1-debian                                             |
|                                                                               |
|  - Route 1: /MainetService/rest/AccountReceiptEntry/doGetPayeeNames           |
|  - Route 2: /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount       |
|                                                                               |
|  (Upstream: host.docker.internal:8080)                                        |
+-------------------------------------------------------------------------------+
        |                                       |
        | etcd sync (:2379)                     | Reverse Proxy HTTP (:8080)
        v                                       v
+-----------------------+       +-----------------------------------------------+
| bitnami/etcd:3.5.11   |       | Local Host Machine (Windows)                  |
| (Control plane store) |       | WildFly 10.1.0.Final Application Server       |
+-----------------------+       |   Listener: 0.0.0.0:8080                      |
                                |   Deployment: MainetService.war               |
                                |     - Spring MVC DispatcherServlet (/rest/*)  |
                                |     - Apache CXF Servlet (/services/*)        |
                                +-----------------------------------------------+
                                                        |
                                                        | JDBC
                                                        v
                                        +-------------------------------+
                                        | MySQL Database                |
                                        | (103.209.145.86:13367)        |
                                        +-------------------------------+
```

### Architectural Principles
- **No Source Code Changes**: `MainetService` remains completely untouched.
- **No Containerization of MainetService**: WildFly runs natively on the Windows host.
- **Direct Path Parity**: Gateway URLs mirror existing WildFly URLs (`http://localhost:9080/MainetService/...` -> `http://localhost:8080/MainetService/...`).
- **No URL Rewriting / Transformations**: URLs and payloads are passed through as-is in Phase 1.

---

## 3. Prerequisites
Before executing the POC, ensure the following components are available on the host machine:

1. **Java Development Kit**:
   - JDK 1.8 (Java 8 64-bit, verified at `D:\Softwares\jdk1.8.0_241`).
2. **Application Server (WildFly / JBoss)**:
   - WildFly 10.1.0.Final (verified at `D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service`).
   - Deployed artifact: `MainetService.war` (active marker: `MainetService.war.deployed`).
3. **Docker Engine & Docker Compose**:
   - Docker Desktop for Windows (with WSL 2 or Hyper-V backend).
   - Ensure `docker` and `docker compose` are available in PATH.
4. **Networking**:
   - Port `8080` available for WildFly HTTP listener.
   - Port `9080` available for APISIX HTTP listener.
   - Port `9180` available for APISIX Admin API.
   - Port `2379` available for etcd.
   - Windows host resolution `host.docker.internal` routed to host gateway.

---

## 4. Directory Structure
All Phase 1 POC files are completely isolated inside `apisix-poc/`:

```
apisix-poc/
├── README.md                      # Comprehensive POC documentation (this file)
├── docker-compose.yml             # APISIX and etcd container definitions
├── apisix_conf/
│   └── config.yaml                # APISIX standalone configuration (etcd, admin, plugins)
├── routes/
│   └── routes.json                # JSON representation of Phase 1 routes
├── scripts/
│   ├── init-routes.ps1            # PowerShell route provisioning script (Admin API)
│   └── init-routes.sh             # Bash route provisioning script (Admin API)
└── test/
    ├── test-payload-accounts.json # Payload for Spring MVC doGetPayeeNames API
    ├── test-payload-purchase.json # Optional payload / placeholder for CXF APIs
    ├── smoke-test.ps1             # PowerShell end-to-end smoke test script
    └── smoke-test.sh              # Bash end-to-end smoke test script
```

---

## 5. Execution Sequence & Diagnostic Boundary

To guarantee unambiguous root-cause isolation, the POC execution follows a strict 5-stage sequential runbook:

```
[Stage 1] Start WildFly :8080
       │
       ▼
[Stage 2] Verify MainetService Directly (:8080)
       │  ├── Fails?  --> Stop. Investigate WildFly / MainetService / MySQL.
       │  └── Passes? --> Proceed to Stage 3.
       ▼
[Stage 3] Start Docker Desktop & APISIX Containers
       │
       ▼
[Stage 4] Provision APISIX Routes via Admin API (:9180)
       │
       ▼
[Stage 5] Execute 4-Way Comparison Smoke Test (:8080 vs :9080)
          ├── Direct Works & Gateway Fails? --> Investigate APISIX / Network / Route.
          └── Both Pass with Parity?        --> Phase 1 Proven Successful!
```

---

### Stage 1: Start and Verify WildFly (Port 8080)

1. Open PowerShell and start the WildFly application server:
   ```powershell
   $env:JAVA_HOME = "D:\Softwares\jdk1.8.0_241"
   & "D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service\bin\standalone.bat" `
     -b 0.0.0.0 `
     -bmanagement 0.0.0.0
   ```

2. In a separate PowerShell terminal, verify that WildFly is actively listening on port 8080:
   ```powershell
   Test-NetConnection localhost -Port 8080
   Get-NetTCPConnection -LocalPort 8080
   ```

---

### Stage 2: Verify MainetService Directly Before APISIX

> [!IMPORTANT]
> **Diagnostic Boundary**: Always prove the direct API works before testing through APISIX. If direct calls fail, **we do not have an APISIX problem**; investigate WildFly, the war deployment, or database connectivity first.

#### A. Direct Spring MVC REST Test
- **Endpoint**: `POST http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
- **Headers**: `Content-Type: application/json`
- **Payload**: `{"orgId": 1}`

```powershell
curl -i -X POST "http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
  -H "Content-Type: application/json" `
  -d '{"orgId":1}'
```

#### B. Direct Apache CXF REST Test
- **Endpoint**: `POST http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`
- **Headers**: `Content-Type: application/json`
- **Payload**: `{}`

```powershell
curl -i -X GET "http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
```

---

### Stage 3: Start Docker Desktop & Launch APISIX

1. Start **Docker Desktop** on the Windows machine and verify Docker CLI availability:
   ```powershell
   docker --version
   docker compose version
   ```

2. Navigate to `apisix-poc/` and start the containers:
   ```powershell
   cd d:\Documents\SKDCL\MainetService\apisix-poc
   docker compose up -d
   ```

3. Verify running containers:
   ```powershell
   docker compose ps
   ```
   Both `apisix-gateway` and `apisix-etcd` must report status `Up`.

---

### Stage 4: Provision APISIX Routes via Admin API

1. Run the route provisioning script:
   ```powershell
   # Windows PowerShell:
   .\scripts\init-routes.ps1

   # Or Bash:
   chmod +x ./scripts/init-routes.sh
   ./scripts/init-routes.sh
   ```

2. Query the APISIX Admin API to confirm both routes are active:
   ```powershell
   curl -i -H "X-API-KEY: <REDACTED_ADMIN_KEY>" http://localhost:9180/apisix/admin/routes
   ```
   Confirm presence of:
   - `mainetservice-accounts-get-payee-names`
   - `mainetservice-common-license-yearwise-count`

3. Verify connectivity from inside the APISIX container to host WildFly:
   ```powershell
   docker exec -it apisix-gateway curl -i -s -o /dev/null -w "%{http_code}\n" http://host.docker.internal:8080/MainetService/
   ```

---

### Stage 5: Execute the Four-Way Comparison Smoke Test

Execute the automated smoke-test suite to test all 4 combinations:

```powershell
# PowerShell:
.\test\smoke-test.ps1

# Or Bash:
./test/smoke-test.sh
```

#### What the test executes:
1. **Test A (Direct Spring MVC)**: `POST http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
2. **Test B (APISIX Spring MVC)**: `POST http://localhost:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
3. **Test C (Direct CXF REST)**: `POST http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`
4. **Test D (APISIX CXF REST)**: `POST http://localhost:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`

#### Manual curl Commands (Through Gateway :9080):
```powershell
# Test B: Spring MVC through APISIX
curl -i -X POST "http://localhost:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
  -H "Content-Type: application/json" `
  -d '{"orgId":1}'

# Test D: CXF REST through APISIX
curl -i -X GET "http://localhost:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
```

---

## 6. How to Verify etcd & APISIX Health

### etcd Health Verification
```powershell
# Inspect etcd container health
docker exec -it apisix-etcd etcdctl endpoint health

# Inspect member list
docker exec -it apisix-etcd etcdctl member list
```
Expected output:
```
127.0.0.1:2379 is healthy: successfully committed proposal: took = 1.2ms
```

### APISIX Container Logs
```powershell
docker compose logs apisix --tail 50
```

---

## 7. Troubleshooting Guide

Use the diagnostic boundary below to isolate whether an issue originates in APISIX, Docker networking, WildFly, or MainetService:

| Symptom / Error | Root Cause Category | Diagnostic Check | Resolution |
| :--- | :--- | :--- | :--- |
| **HTTP 404 Not Found** *(Server: APISIX)* | **APISIX Routing** | APISIX returned 404 with `{"error_msg":"404 Route Not Found"}`. Route URI or HTTP method mismatch. | Verify route in Admin API (`curl http://localhost:9180/apisix/admin/routes`). Ensure method is `POST` and URI exact prefix matches. Re-run `init-routes.ps1`. |
| **HTTP 404 Not Found** *(Server: WildFly/JBoss)* | **MainetService Endpoint** | WildFly returned HTML 404. APISIX successfully forwarded request, but WildFly or Servlet context is missing. | Verify WildFly deployment: Check if `MainetService.war.deployed` exists in `standalone/deployments/`. Verify servlet context path `/MainetService/`. |
| **HTTP 502 Bad Gateway** | **Network / Connectivity** | APISIX cannot establish a TCP connection to upstream `host.docker.internal:8080`. | 1. Check if WildFly is running: `netstat -ano \| findstr 8080`.<br>2. Test connection inside APISIX container: `docker exec apisix-gateway curl -v http://host.docker.internal:8080`.<br>3. Check Windows Firewall: allow inbound port 8080 from Docker vEthernet subnet. |
| **HTTP 504 Gateway Timeout** | **WildFly / Database** | Upstream accepted TCP connection but failed to return HTTP response within `read_timeout` (60s). | Check WildFly `server.log`. Commonly caused by remote MySQL database timeouts (`103.209.145.86:13367`), deadlocks, or long-running queries in `MainetService`. |
| **Connection Refused on 8080** | **WildFly Inactive** | WildFly service is stopped or bound exclusively to `127.0.0.1` rather than `0.0.0.0`. | Start WildFly using `standalone.bat -b 0.0.0.0 -bmanagement 0.0.0.0`. |
| **APISIX Container Fails to Start** | **Port Conflict / Docker** | Port `9080`, `9180`, or `2379` already occupied by another local service. | Check occupied ports: `netstat -ano \| findstr "9080 9180 2379"`. Free conflicting ports or adjust bindings in `docker-compose.yml`. |
| **etcd Unavailable** | **Container Health** | `apisix-etcd` container crashing or directory permissions issue. | Check logs: `docker logs apisix-etcd`. Verify `ALLOW_NONE_AUTHENTICATION=yes` and `ETCD_ENABLE_V2=true` in `docker-compose.yml`. |
| **Incorrect Content-Type** | **Client / Proxy Header** | Client sends form-data or text/plain instead of `application/json`. | Ensure `Content-Type: application/json` header is explicitly sent in client request. In APISIX, `pass_host: "pass"` preserves client headers. |
| **Request Body Not Forwarded** | **Payload Truncation** | Client request has empty body or `Transfer-Encoding: chunked` issue. | Ensure request body is valid JSON. In APISIX route config, standard POST routes buffer and proxy request bodies transparently. |

---

## 8. Security Baseline & Known Limitations

> [!CAUTION]
> **Current Security Baseline — NOT Production Acceptable:**
> In Phase 1, both the Spring MVC REST and Apache CXF REST endpoints are accessible anonymously through APISIX without authentication tokens, API keys, or session cookies. This matches the existing behavior of the unauthenticated legacy REST endpoints in `MainetService`. **Do not expose this configuration to public or untrusted networks.**

### Known Phase 1 Limitations
1. **No URL Abstraction / Versioning**: Consumers still call the legacy path `/MainetService/...` instead of a modern standardized path (e.g. `/api/v1/...`).
2. **No Rate Limiting or DDoS Protection**: Endpoints can be flooded without restriction.
3. **No Authentication / Authorization**: Requests are not validated against Keycloak or any OAuth2/OIDC Identity Provider.
4. **No Observability Pipeline**: Prometheus metrics and OpenTelemetry tracing are not integrated into an enterprise monitoring dashboard.
5. **Single-Node Upstream**: High availability and active health checks across clustered WildFly worker nodes are not enabled.

---

## 9. Phase 2 Plan (Future Roadmap)

In Phase 2, the following capabilities will be designed and evaluated:

1. **Path Modernization & URL Rewriting (`proxy-rewrite`)**:
   - Introduce clean modern paths for external API consumers:
     - `/api/v1/accounts/payee-names` -> rewrites to `/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
     - `/api/v1/materialmgmt/requisitions/{deptId}/{orgId}` -> rewrites to `/MainetService/services/materialmgmt/rest/PurchaseReq/fetchPurchaseRequistion/{deptId}/{orgId}`
2. **Rate Limiting & Traffic Shaping (`limit-count`, `limit-req`)**:
   - Enforce consumer rate limits (e.g. 100 req/min per client IP or API key) to protect WildFly threads and database connection pools from exhaustion.
3. **Correlation Tracking & Observability (`request-id`, `prometheus`)**:
   - Inject unique `X-Request-Id` UUIDs on all incoming client requests and propagate them to WildFly for end-to-end distributed tracing in logs.
   - Scrape APISIX gateway metrics via `/apisix/prometheus/metrics` into Prometheus and Grafana dashboards.
4. **Standardized Response Headers & CORS (`cors`, `response-rewrite`)**:
   - Uniform CORS configuration for Single Page Applications (SPAs).
   - Inject standard security headers (`X-Content-Type-Options`, `X-Frame-Options`).
5. **Resilience & Failure Handling**:
   - Gateway timeout overrides, automatic retries on idempotent calls, and circuit-breaking (`api-breaker`).

*(Phase 3 will subsequently evaluate OAuth2/OIDC token validation with Keycloak, JWT verification, and user identity propagation into MainetService).*

---

## 10. Phase 1 Runtime Validation

### 1. Execution Date / Time
- **Timestamp**: 2026-09-28 14:18:57 – 14:32:17 IST

### 2. Environment
- **Operating System**: Windows 10 Pro (x64)
- **Host Name / IP**: `192.168.200.126` (Wi-Fi)
- **Active User Workspace**: `d:\Documents\SKDCL\MainetService`

### 3. Java Version
- **Command Output**:
  ```text
  java version "1.8.0_241"
  Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
  Java HotSpot(TM) 64-Bit Server VM (build 25.241-b07, mixed mode)
  ```
- **JAVA_HOME**: `D:\Softwares\jdk1.8.0_241`

### 4. WildFly Version & Deployment Status
- **WildFly Version**: WildFly Full 10.1.0.Final (WildFly Core 2.2.0.Final)
- **Installation Path**: `D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service`
- **Deployment Artifact**: `MainetService.war` (214,988,068 bytes, active marker `MainetService.war.deployed`)
- **Remote Database Dependency**: MySQL at `103.209.145.86:13367` (Verified TCP connection: `TcpTestSucceeded : True`)

### 5. APISIX Version
- **Specified Image**: `apache/apisix:3.9.1-debian` (Docker Compose configuration prepared)

### 6. etcd Version
- **Specified Image**: `bitnami/etcd:3.5.11` (Docker Compose configuration prepared)

### 7. Docker Version
- **Verification Attempt**:
  ```powershell
  docker --version
  docker compose version
  docker version
  ```
- **Output**:
  ```text
  CommandNotFoundException: The term 'docker' is not recognized as the name of a cmdlet, function, script file, or operable program.
  ```
- **Result**: Docker Desktop is not installed or available on this host.

### 8. Direct Spring MVC Result
- **URL**: `POST http://localhost:8080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
- **Payload**: `{"orgId": 1}`
- **HTTP Status**: N/A (Connection Refused / Failed)
- **Error**: `Unable to connect to the remote server`

### 9. Direct CXF Result
- **URL**: `POST http://localhost:8080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`
- **Payload**: `{}`
- **HTTP Status**: N/A (Connection Refused / Failed)
- **Error**: `Unable to connect to the remote server`

### 10. APISIX Spring MVC Result
- **URL**: `POST http://localhost:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames`
- **HTTP Status**: Not executed (Blocked at Diagnostic Gate & Docker Gate)

### 11. APISIX CXF Result
- **URL**: `POST http://localhost:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount`
- **HTTP Status**: Not executed (Blocked at Diagnostic Gate & Docker Gate)

### 12. Four-Way Test Matrix
| Test ID | Path / Target | Port | Protocol | Status Code | Result | Note |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Test A** | Direct Spring MVC | 8080 | HTTP POST | 000 | **BLOCKED** | WildFly rolled back deployment on 300s timeout. |
| **Test B** | APISIX Spring MVC | 9080 | HTTP POST | --- | **BLOCKED** | Docker daemon unavailable on host. |
| **Test C** | Direct CXF REST | 8080 | HTTP POST | 000 | **BLOCKED** | WildFly rolled back deployment on 300s timeout. |
| **Test D** | APISIX CXF REST | 9080 | HTTP POST | --- | **BLOCKED** | Docker daemon unavailable on host. |

### 13. Response Comparison
- Because both direct and APISIX endpoints could not complete an HTTP response cycle, no response parity comparison could be generated.

### 14. Observed Latency
- **Spring MVC Direct**: N/A
- **Spring MVC APISIX**: N/A
- **CXF Direct**: N/A
- **CXF APISIX**: N/A

### 15. Problems Encountered
1. **WildFly Deployment Timeout (`WFLYCTL0348`)**:
   - During boot, WildFly bound port 8080 (`Undertow HTTP listener default listening on 0.0.0.0:8080`) and began deploying `MainetService.war` (214 MB).
   - At exactly 300 seconds, the WildFly Management Controller triggered a timeout and rollback:
     ```text
     ERROR [org.jboss.as.controller.management-operation] (Controller Boot Thread) WFLYCTL0348: Timeout after [300] seconds waiting for service container stability. Operation will roll back.
     FATAL [org.jboss.as.server] (Controller Boot Thread) WFLYSRV0056: Server boot has failed in an unrecoverable manner; exiting.
     ```
   - Classification: **F. MainetService deployment problem / E. WildFly container timeout**.
2. **Docker Unavailable on Host**:
   - `docker` and `docker compose` executables are missing from system PATH (`CommandNotFoundException`).
   - Classification: **C. Docker/network problem**.

### 16. Resolution
1. **To Resolve WildFly Boot Timeout**:
   - WildFly default deployment timeout is 300 seconds. For large legacy enterprise WARs (214 MB) loading Liquibase, Hibernate models, and remote MySQL catalogs (`103.209.145.86:13367`), the timeout parameter must be extended without changing application code:
     ```powershell
     & "D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service\bin\standalone.bat" `
       -b 0.0.0.0 `
       -bmanagement 0.0.0.0 `
       -Djboss.as.management.blocking.timeout=900
     ```
2. **To Resolve Docker Availability**:
   - Install and launch Docker Desktop for Windows with WSL 2 backend.
   - Run `docker compose up -d` in `d:\Documents\SKDCL\MainetService\apisix-poc`.

### 17. Final Phase 1 Status
**BLOCKED**

*(In accordance with the project acceptance criteria, Phase 1 cannot be declared VALIDATED because real HTTP requests could not complete through the end-to-end chain).*

