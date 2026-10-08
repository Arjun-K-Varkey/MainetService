# Apache APISIX + MainetService
# Phase 2-A POC Evidence: Request ID / Correlation ID

---

## 1. Document Control

| Property | Value |
| :--- | :--- |
| **Document Title** | Apache APISIX + MainetService Phase 2-A POC Evidence: Request ID / Correlation ID |
| **POC Name** | Apache APISIX Inbound Gateway Integration for MainetService |
| **Phase** | Phase 2-A: Request ID & Correlation Tracking |
| **Date** | 30 September 2026 |
| **Environment** | Local POC (Windows 10 Host + Docker Desktop) |
| **Status** | **Phase 2-A Functionally Validated\*** |
| **Scope** | Route-level Request ID injection, client header preservation, and response propagation |

> [!NOTE]
> **Interim Documentation Notice**: This document is an interim technical evidence report specifically evaluating Phase 2-A (Request ID). Phase 1 transparent proxying is frozen as baseline and documented in [APISIX_MainetService_POC_Phase1_Evidence.md](file:///d:/Documents/SKDCL/MainetService/apisix-poc/docs/APISIX_MainetService_POC_Phase1_Evidence.md). Subsequent phases (2-B Rate Limiting, 2-C Prometheus, 2-D Auth) will follow in separate dedicated iterations.

> [!IMPORTANT]
> **Evidence Integrity Statement**: All findings and metrics recorded herein derive strictly from direct execution within the local POC environment. No performance benchmarks, production throughput claims, or high-availability assertions are made.

---

## 2. Objective

The objective of Phase 2-A is to evaluate and validate the standardized request tracking and correlation identifier capabilities provided by Apache APISIX when placed in front of the unmodified legacy MainetService application.

Specifically, Phase 2-A establishes:
1. Behavior when the client **does not provide** an `X-Request-ID` header.
2. Behavior when the client **provides** an explicit `X-Request-ID` header.
3. Whether APISIX returns the request ID in the downstream HTTP response header.
4. Whether APISIX sets/propagates the request ID on the upstream request context.
5. Whether request correlation operates uniformly across both legacy frameworks:
   - Spring MVC REST (`/MainetService/rest/*`)
   - Apache CXF / JAX-RS REST (`/MainetService/services/*`)
6. Whether underlying MainetService business responses remain completely unchanged.

---

## 3. Phase-1 Baseline Reference

Phase 1 established that Apache APISIX 3.9.1 acts as a transparent reverse proxy for MainetService running on WildFly 10.1.0.Final (Java 8) without any modifications to application code or deployment archives.

Key baseline facts:
- **Spring MVC Route**: `POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames` with body `{"orgId":1}` returns application-level `HTTP 404` (`"No Record found for provided input"`).
- **Apache CXF Route**: `GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount` returns `HTTP 200` with active license records for years 2022–2027.
- **Upstream Host**: `host.docker.internal:8080` (resolving to Docker gateway `192.168.65.254:8080`).
- **Control Plane**: etcd 3.5.11 managing dynamic route updates.

---

## 4. Request ID Architecture

### Logical Architecture Flow

```
Client (Consumer)
   │
   │  [Case 1: No Header]                   [Case 2: Client Header]
   │  Request: GET / POST                   Request: GET / POST
   │  (No X-Request-ID)                     X-Request-ID: POC-REQ-20260930-001
   ▼                                        ▼
+───────────────────────────────────────────────────────────────────────────────+
| Apache APISIX Gateway Container (:9080)                                       |
| Plugin: request-id (Priority: 12015, Phase: rewrite & header_filter)          |
|                                                                               |
| 1. Inbound Inspection:                                                        |
|    - If header missing: generates UUID v4 request identifier                  |
|    - If header present: preserves client value                                |
| 2. Upstream Context:                                                          |
|    - Injects header into ngx.req for upstream proxy pass                      |
| 3. Response Injection:                                                        |
|    - Attaches 'X-Request-Id: <id>' to downstream response                     |
+───────────────────────────────────────────────────────────────────────────────+
   │                                        │
   │ Upstream Request with                  │ Upstream Request with
   │ X-Request-Id: <generated-uuid>         │ X-Request-Id: POC-REQ-20260930-001
   ▼                                        ▼
+───────────────────────────────────────────────────────────────────────────────+
| WildFly 10.1.0.Final Host Machine (:8080)                                     |
| MainetService.war (Spring MVC / Apache CXF)                                   |
| Executes application business logic                                           |
+───────────────────────────────────────────────────────────────────────────────+
   │                                        │
   │ Downstream Response with               │ Downstream Response with
   │ X-Request-Id: <generated-uuid>         │ X-Request-Id: POC-REQ-20260930-001
   ▼                                        ▼
Client Receives Verified Correlation ID
```

### Distinction of Identifiers
- **APISIX Internal Request Identifier**: OpenResty/Nginx internal connection ID (`ngx.var.connection`, `ngx.var.connection_requests`).
- **HTTP Header `X-Request-Id`**: The RFC-standard correlation identifier exchanged between client, gateway, and upstream. It is a correlation identifier, not an authentication token or security credential.
- **MainetService Application Session**: Internal Java JSESSIONID or database transaction IDs.

Phase 2-A focuses exclusively on HTTP Header `X-Request-Id` at the gateway layer.

---

## 5. Implementation

### Plugin Availability
The `request-id` plugin is pre-compiled into `apache/apisix:3.9.1-debian` and enabled in `apisix_conf/config.yaml`.

### Route-Level Configuration
In accordance with least-privilege principles, the plugin was attached strictly at the **route level** on both existing POC routes via the APISIX Admin API (`PUT /apisix/admin/routes/{id}`). The route definitions were changed solely by adding the `plugins.request-id` block. The route URIs, HTTP methods, and upstream definitions remained strictly unchanged.

#### Plugin JSON Schema on Routes
```json
"plugins": {
  "request-id": {
    "include_in_response": true
  }
}
```

#### Complete Route 1 Definition (Spring MVC)
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
    "timeout": { "connect": 6, "send": 6, "read": 60 },
    "pass_host": "pass"
  },
  "plugins": {
    "request-id": {
      "include_in_response": true
    }
  },
  "status": 1
}
```

#### Complete Route 2 Definition (Apache CXF)
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
    "timeout": { "connect": 6, "send": 6, "read": 60 },
    "pass_host": "pass"
  },
  "plugins": {
    "request-id": {
      "include_in_response": true
    }
  },
  "status": 1
}
```

---

## 6. Test Environment

- **Operating System**: Windows 10 Pro (x64)
- **Application Server**: WildFly 10.1.0.Final (`D:\Softwares\wildfly-service\wildfly-10.1.0.Final-PSCL_Service`)
- **JDK**: Oracle JDK 1.8.0_241 (`D:\Softwares\jdk1.8.0_241`)
- **Gateway**: Apache APISIX 3.9.1 on Docker Desktop (WSL 2 backend)
- **Data Store**: bitnamilegacy/etcd:3.5.11
- **Gateway HTTP Port**: `9080`
- **Gateway Admin Port**: `9180`
- **WildFly HTTP Port**: `8080`

---

## 7. Test A — No Client X-Request-ID

### Test A.1: Spring MVC Route (POST)
- **Command**:
  ```bash
  curl.exe -i -s -X POST "http://127.0.0.1:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
    -H "Content-Type: application/json" \
    --data-binary "@test/test-payload-accounts.json"
  ```
- **Inbound Request Headers**: Standard HTTP headers, **no `X-Request-ID`**.
- **Downstream Response Received**:
  ```http
  HTTP/1.1 404 Not Found
  Content-Type: application/json; charset=UTF-8
  Transfer-Encoding: chunked
  Connection: keep-alive
  X-Powered-By: Undertow/1
  Date: Wed, 30 Sep 2026 08:34:58 GMT
  Server: APISIX/3.9.1
  X-Request-Id: 701f2bc2-e369-43a0-906b-da1768c660ed

  "No Record found for provided input"
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:08:34:58 +0000] 127.0.0.1:9080 "POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames HTTP/1.1" 404 47 7.415 "-" "curl/8.21.0" 192.168.65.254:8080 404 7.416 "http://127.0.0.1:9080"
  ```
- **Observation**: APISIX generates a UUID v4 request identifier (`701f2bc2-e369-43a0-906b-da1768c660ed`) and returns it in the response header `X-Request-Id`. Application 404 response body was preserved.

### Test A.2: Apache CXF Route (GET)
- **Command**:
  ```bash
  curl.exe -i -s -X GET "http://127.0.0.1:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
  ```
- **Inbound Request Headers**: Standard HTTP headers, **no `X-Request-ID`**.
- **Downstream Response Received**:
  ```http
  HTTP/1.1 200 OK
  Content-Type: application/json; charset=UTF-8
  Transfer-Encoding: chunked
  Connection: keep-alive
  X-Powered-By: Undertow/1
  Access-Control-Allow-Headers: Content-Type
  Date: Wed, 30 Sep 2026 08:35:23 GMT
  Access-Control-Allow-Origin: *
  Access-Control-Allow-Methods: GET, POST, DELETE, PUT
  Server: APISIX/3.9.1
  X-Request-Id: 8f5220b7-2f13-4c53-a959-3dfd93861d82

  [{"id":1,"year":"2027","storageLicenseCount":0,"tradeLicenseCount":0},{"id":2,"year":"2026","storageLicenseCount":0,"tradeLicenseCount":0},{"id":3,"year":"2025","storageLicenseCount":0,"tradeLicenseCount":0},{"id":4,"year":"2024","storageLicenseCount":0,"tradeLicenseCount":0},{"id":5,"year":"2023","storageLicenseCount":0,"tradeLicenseCount":0},{"id":6,"year":"2022","storageLicenseCount":0,"tradeLicenseCount":0}]
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:08:35:24 +0000] 127.0.0.1:9080 "GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount HTTP/1.1" 200 427 5.119 "-" "curl/8.21.0" 192.168.65.254:8080 200 5.118 "http://127.0.0.1:9080"
  ```
- **Observation**: APISIX generates a UUID v4 request identifier (`8f5220b7-2f13-4c53-a959-3dfd93861d82`) and returns it in the response header `X-Request-Id`. Full CXF JSON dataset was preserved.

---

## 8. Test B — Client-Supplied X-Request-ID

### Test B.1: First Test Value (`POC-REQ-20260930-001`)

#### Spring MVC Route (POST)
- **Command**:
  ```bash
  curl.exe -i -s -X POST "http://127.0.0.1:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
    -H "Content-Type: application/json" \
    -H "X-Request-ID: POC-REQ-20260930-001" \
    --data-binary "@test/test-payload-accounts.json"
  ```
- **Downstream Response Received**:
  ```http
  HTTP/1.1 404 Not Found
  Content-Type: application/json; charset=UTF-8
  Transfer-Encoding: chunked
  Connection: keep-alive
  X-Powered-By: Undertow/1
  Date: Wed, 30 Sep 2026 08:39:53 GMT
  Server: APISIX/3.9.1
  X-Request-Id: POC-REQ-20260930-001

  "No Record found for provided input"
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:08:39:53 +0000] 127.0.0.1:9080 "POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames HTTP/1.1" 404 47 1.318 "-" "curl/8.21.0" 192.168.65.254:8080 404 1.317 "http://127.0.0.1:9080"
  ```
- **Result**: Preserved exact client-supplied string `POC-REQ-20260930-001`.

#### Apache CXF Route (GET)
- **Command**:
  ```bash
  curl.exe -i -s -X GET "http://127.0.0.1:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" \
    -H "X-Request-ID: POC-REQ-20260930-001"
  ```
- **Downstream Response Received**:
  ```http
  HTTP/1.1 200 OK
  Content-Type: application/json; charset=UTF-8
  Transfer-Encoding: chunked
  Connection: keep-alive
  X-Powered-By: Undertow/1
  Access-Control-Allow-Headers: Content-Type
  Date: Wed, 30 Sep 2026 08:45:30 GMT
  Access-Control-Allow-Origin: *
  Access-Control-Allow-Methods: GET, POST, DELETE, PUT
  Server: APISIX/3.9.1
  X-Request-Id: POC-REQ-20260930-001

  [{"id":1,"year":"2027","storageLicenseCount":0,"tradeLicenseCount":0},...]
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:08:45:31 +0000] 127.0.0.1:9080 "GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount HTTP/1.1" 200 427 10.661 "-" "curl/8.21.0" 192.168.65.254:8080 200 10.662 "http://127.0.0.1:9080"
  ```
- **Result**: Preserved exact client-supplied string `POC-REQ-20260930-001`.

---

### Test B.2: Second Test Value (`POC-REQ-20260930-002`)

To confirm deterministic, non-accidental behavior, a second identifier was evaluated:

#### Spring MVC Route (POST)
- **Inbound Header**: `X-Request-ID: POC-REQ-20260930-002`
- **Downstream Response Received**:
  ```http
  HTTP/1.1 404 Not Found
  ...
  X-Request-Id: POC-REQ-20260930-002

  "No Record found for provided input"
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:08:50:22 +0000] 127.0.0.1:9080 "POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames HTTP/1.1" 404 47 3.604 "-" "curl/8.21.0" 192.168.65.254:8080 404 3.604 "http://127.0.0.1:9080"
  ```

#### Apache CXF Route (GET)
- **Inbound Header**: `X-Request-ID: POC-REQ-20260930-002`
- **Downstream Response Received**:
  ```http
  HTTP/1.1 200 OK
  ...
  X-Request-Id: POC-REQ-20260930-002

  [{"id":1,"year":"2027","storageLicenseCount":0,"tradeLicenseCount":0},...]
  ```
- **Gateway Access Log**:
  ```text
  172.18.0.1 - - [30/Sep/2026:09:01:52 +0000] 127.0.0.1:9080 "GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount HTTP/1.1" 200 427 3.990 "-" "curl/8.21.0" 192.168.65.254:8080 200 3.996 "http://127.0.0.1:9080"
  ```

### Conclusion on Header Preservation
**VALIDATED**: APISIX deterministically inspects the inbound request. If a client provides `X-Request-ID` (or `x-request-id`), APISIX does **not** overwrite it with a new UUID; it preserves the client-supplied value and echoes it in the response header `X-Request-Id`.

---

## 9. Upstream Propagation Findings

### Source Code Analysis (`request-id.lua`)
Inspection of the Apache APISIX 3.9.1 container implementation (`/usr/local/apisix/apisix/plugins/request-id.lua`) establishes the internal mechanism:
```lua
function _M.rewrite(conf, ctx)
    local headers = ngx.req.get_headers()
    local uuid_val
    if not headers[conf.header_name] then
        uuid_val = get_request_id(conf)
        core.request.set_header(ctx, conf.header_name, uuid_val)
    else
        uuid_val = headers[conf.header_name]
    end

    if conf.include_in_response then
        ctx["request-id-" .. conf.header_name] = uuid_val
    end
end
```
When `headers[conf.header_name]` is absent, APISIX calls `core.request.set_header(ctx, conf.header_name, uuid_val)`, which mutates `ngx.req` headers prior to the OpenResty `proxy_pass` dispatch.

### Runtime Verification Boundary
The boundary of what is empirically validated versus unobservable is:

```text
Client → APISIX
          │
          ├── Request ID generation/preservation: VERIFIED
          │
          ▼
      downstream response
          │
          └── Request ID: VERIFIED

APISIX → MainetService
          │
          └── actual receipt by WildFly: NOT INDEPENDENTLY VERIFIED
```

- **MainetService Code**: Contains no servlet filters or interceptors that extract or log `X-Request-Id`.
- **WildFly Undertow Logger**: The logging pattern in `standalone.xml` is configured for `%d %-5p [%c] (%t) %s%e%n` (timestamp, priority, category, thread, message), without `%i{X-Request-Id}` header extraction.
- **Strict Project Constraint**: In accordance with the safety rules, MainetService source code and WildFly configuration were **not modified** merely to observe the header.

### Formal Upstream Status
**NOT INDEPENDENTLY VERIFIED**: Upstream propagation into MainetService application logs could not be independently verified in this POC without modifying the legacy application or server configuration. While the APISIX codebase confirms that `core.request.set_header` is invoked on `ngx.req`, actual receipt by WildFly Undertow remains unobservable without code/config instrumentation.

---

## 10. Spring MVC Regression Analysis

| Dimension | Phase-1 Baseline | Phase 2-A Result | Status |
| :--- | :--- | :--- | :---: |
| **HTTP Status Code** | 404 Not Found | 404 Not Found | **PASS** |
| **Response Body** | `"No Record found for provided input"` | `"No Record found for provided input"` | **PASS** |
| **Content-Type** | `application/json; charset=UTF-8` | `application/json; charset=UTF-8` | **PASS** |
| **Server Header** | `APISIX/3.9.1` | `APISIX/3.9.1` | **PASS** |
| **Upstream Status** | 404 | 404 | **PASS** |
| **Correlation Header** | None | `X-Request-Id` present | **PASS** |

The addition of the `request-id` plugin caused zero regression to Spring MVC application semantics.

---

## 11. Apache CXF Regression Analysis

| Dimension | Phase-1 Baseline | Phase 2-A Result | Status |
| :--- | :--- | :--- | :---: |
| **HTTP Status Code** | 200 OK | 200 OK | **PASS** |
| **Response Body** | 2022–2027 year-wise license counts JSON | Identical 2022–2027 dataset | **PASS** |
| **Content-Type** | `application/json; charset=UTF-8` | `application/json; charset=UTF-8` | **PASS** |
| **CORS Headers** | Undertow CORS headers preserved | Undertow CORS headers preserved | **PASS** |
| **Server Header** | `APISIX/3.9.1` | `APISIX/3.9.1` | **PASS** |
| **Correlation Header** | None | `X-Request-Id` present | **PASS** |

The addition of the `request-id` plugin caused zero regression to Apache CXF application semantics.

---

## 12. APISIX Restart Validation

To evaluate route and plugin behavior across gateway lifecycles:

1. **Restart Triggered**: `docker compose restart apisix`.
2. **Container State**: Container restarted and reported healthy (`Container apisix-gateway Started`).
3. **Admin API Verification Post-Restart**:
   - Query: `GET http://localhost:9180/apisix/admin/routes`
   - Result: Both routes `mainetservice-accounts-get-payee-names` and `mainetservice-common-license-yearwise-count` retained their complete `plugins.request-id` definitions in etcd.
4. **Post-Restart Request Execution**:
   - An invocation with `X-Request-ID: POC-REQ-POSTRESTART-001` was sent to the CXF route.
   - The test did not complete with an HTTP 200 response, returning an upstream timeout / error.
   - An error response header containing `X-Request-Id: POC-REQ-POSTRESTART-001` was observed, but because the request did not produce a successful application response, this test cannot serve as conclusive evidence that end-to-end Request-ID handling remained fully functional post-restart.

### Diagnostic Note on Post-Restart Test
Post-restart end-to-end Request-ID validation remains inconclusive because the CXF request returned HTTP 504 from the gateway due to an upstream timeout. APISIX accepted and returned the supplied Request-ID, but successful downstream application processing could not be validated.

### Conclusion on Restart
**Route configuration survived gateway restart; post-restart end-to-end Request-ID validation remains inconclusive because the CXF request returned HTTP 504 from the gateway due to an upstream timeout. APISIX accepted and returned the supplied Request-ID, but successful downstream application processing could not be validated.**

---

## 13. Evidence Matrix

| # | Test Scenario | Target Route | Client Header | Expected Response Header | Actual Response Header | Status |
| :-: | :--- | :--- | :--- | :--- | :--- | :---: |
| **1** | Default Generation | Spring MVC | *(None)* | Generated UUID v4 | `701f2bc2-e369-43a0-906b-da1768c660ed` | **VALIDATED** |
| **2** | Default Generation | Apache CXF | *(None)* | Generated UUID v4 | `8f5220b7-2f13-4c53-a959-3dfd93861d82` | **VALIDATED** |
| **3** | Client Preservation 1 | Spring MVC | `POC-REQ-20260930-001` | `POC-REQ-20260930-001` | `POC-REQ-20260930-001` | **VALIDATED** |
| **4** | Client Preservation 1 | Apache CXF | `POC-REQ-20260930-001` | `POC-REQ-20260930-001` | `POC-REQ-20260930-001` | **VALIDATED** |
| **5** | Client Preservation 2 | Spring MVC | `POC-REQ-20260930-002` | `POC-REQ-20260930-002` | `POC-REQ-20260930-002` | **VALIDATED** |
| **6** | Client Preservation 2 | Apache CXF | `POC-REQ-20260930-002` | `POC-REQ-20260930-002` | `POC-REQ-20260930-002` | **VALIDATED** |
| **7** | Route Persistence | Admin API | N/A | Plugin present after restart | Verified in etcd route JSON | **VALIDATED** |
| **8** | Post-Restart Traffic | Apache CXF | `POC-REQ-POSTRESTART-001` | `POC-REQ-POSTRESTART-001` with 200 OK | Inconclusive (upstream timeout) | **NOT VERIFIED** |
| **9** | Upstream Receipt | WildFly | Any | Visible in application log | Unobservable without app change | **NOT VERIFIED** |
| **10**| Distributed Tracing | OpenTelemetry | Any | Trace spans exported | Not tested in Phase 2-A | **NOT TESTED** |

---

## 14. Phase 2-A Acceptance Matrix

| Acceptance Item | Target Expectation | Finding | Status |
| :--- | :--- | :--- | :---: |
| **Request ID Plugin Configuration** | Route-level configuration applied | Applied via Admin API to both routes | **PASS** |
| **No Unrelated Features** | Zero additional plugins or rewrites | Rate limiting, OAuth, Prometheus excluded | **PASS** |
| **Request Without Header** | Generates UUID v4 request ID | Verified on Spring MVC and CXF | **PASS** |
| **Request With Header** | Preserves client-supplied value | Verified with multiple test tokens | **PASS** |
| **Response Header Delivery** | Echoes `X-Request-Id` to client | Verified across all test requests | **PASS** |
| **Upstream Propagation** | Document whether reached WildFly | Documented as unobservable | **NOT VERIFIED** |
| **Spring MVC Regression** | 404 and exact body preserved | Zero semantic drift | **PASS** |
| **CXF Regression** | 200 and exact dataset preserved | Zero semantic drift | **PASS** |
| **Route Persistence After Restart** | Route definitions survive restart | Verified via Admin API in etcd | **PASS** |
| **Request-ID After Gateway Restart** | End-to-end success post-restart | Inconclusive due to upstream timeout | **NOT VERIFIED** |
| **Phase-1 Routes Preserved** | Existing paths and upstreams intact | URIs, methods, upstreams untouched | **PASS** |

---

## 15. Known Limitations & Security Considerations

### Known Limitations
1. **Header Name Casing**: APISIX generates the header with casing `X-Request-Id` (capital X, R, I; lowercase d). While HTTP/1.1 header names are case-insensitive, legacy systems performing exact case comparisons must account for this format.
2. **No Upstream Confirmation**: Without application logging changes, upstream services cannot confirm receipt of the correlation header.
3. **No Centralized Log Aggregation**: Generated request IDs currently reside in downstream response headers and APISIX access logs; they are not yet shipped to Elasticsearch, Loki, or Splunk.
4. **No Cryptographic Signing**: Client-supplied request IDs are accepted as-is without validation against forged identifiers.

### Security Considerations
- **Correlation ID vs Security Credential**: The Request ID is strictly a **correlation identifier** for traceability across logs; it must not be treated as a security credential, token, or authentication assertion.
- **Client Header Trust**: APISIX echoes client-supplied `X-Request-ID` headers without sanitization. In public perimeter deployments, architects should determine whether client IDs should be accepted or stripped in favor of mandatory gateway-generated UUIDs.
- **Admin API Key Safety**: In all scripts, configurations, and documentation, the APISIX Admin API key is protected and redacted as `<REDACTED>`.

---

## 16. Phase 2-A Conclusion

Phase 2-A Request-ID functionality is functionally validated for the two configured MainetService routes under normal gateway operation. Client-supplied Request IDs are preserved and generated Request IDs are returned in the response. Application response behavior remains unchanged. Upstream receipt of the generated identifier was not independently verified. Post-restart end-to-end Request-ID validation remains inconclusive because the CXF request returned HTTP 504 from the gateway due to an upstream timeout. APISIX accepted and returned the supplied Request-ID, but successful downstream application processing could not be validated.

### Summary Assessment

```text
PHASE 2-A

Request ID generation          PASS
Client ID preservation         PASS
Response ID                    PASS
Spring MVC regression          PASS
CXF regression                 PASS
Upstream propagation           NOT VERIFIED
Route persistence              PASS
Post-restart Request ID        NOT VERIFIED
Overall                        FUNCTIONALLY VALIDATED*
```

> `* Functional validation applies to normal gateway operation; post-restart Request-ID validation remains outstanding.`

---

## 17. Appendix — Commands and Configuration

### 1. Route Provisioning Command (PowerShell)
```powershell
$adminUrl = "http://localhost:9180/apisix/admin"
$apiKey   = "<REDACTED>"
$headers  = @{
    "X-API-KEY"    = $apiKey
    "Content-Type" = "application/json"
}

# Attach request-id to Spring MVC route
$route1 = @{
    name     = "mainetservice-accounts-get-payee-names"
    uri      = "/MainetService/rest/AccountReceiptEntry/doGetPayeeNames"
    methods  = @("POST")
    upstream = @{
        type      = "roundrobin"
        nodes     = @{ "host.docker.internal:8080" = 1 }
        timeout   = @{ connect = 6; send = 6; read = 60 }
        pass_host = "pass"
    }
    plugins  = @{ "request-id" = @{ "include_in_response" = $true } }
    status   = 1
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Uri "$adminUrl/routes/mainetservice-accounts-get-payee-names" -Method Put -Headers $headers -Body $route1

# Attach request-id to CXF route
$route2 = @{
    name     = "mainetservice-common-license-yearwise-count"
    uri      = "/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
    methods  = @("GET")
    upstream = @{
        type      = "roundrobin"
        nodes     = @{ "host.docker.internal:8080" = 1 }
        timeout   = @{ connect = 6; send = 6; read = 60 }
        pass_host = "pass"
    }
    plugins  = @{ "request-id" = @{ "include_in_response" = $true } }
    status   = 1
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Uri "$adminUrl/routes/mainetservice-common-license-yearwise-count" -Method Put -Headers $headers -Body $route2
```

### 2. Validation curl Invocations
```bash
# Test A: No Header
curl.exe -i -X POST "http://127.0.0.1:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
  -H "Content-Type: application/json" --data-binary "@test/test-payload-accounts.json"

curl.exe -i -X GET "http://127.0.0.1:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"

# Test B: Client Header
curl.exe -i -X POST "http://127.0.0.1:9080/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
  -H "Content-Type: application/json" -H "X-Request-ID: POC-REQ-20260930-001" \
  --data-binary "@test/test-payload-accounts.json"

curl.exe -i -X GET "http://127.0.0.1:9080/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" \
  -H "X-Request-ID: POC-REQ-20260930-001"
```

### 3. Gateway Restart Command
```bash
docker compose restart apisix
```
