# MainetServiceWeb to Backend Modules Relationship

## 1. Overview

In the MainetService multi-module architecture, **`MainetServiceWeb`** serves as the unified Web Application packaging and servlet orchestration layer, while **`MainetServiceAccount`** and **`MainetServiceMaterialMgmt`** supply domain business services and REST controllers.

This document details the precise technical relationship, packaging structure, and invocation pathways between the web layer and backend modules.

---

## 2. Structural & Packaging Relationship

```
                       +-------------------------+
                       |    MainetServiceWeb     |
                       |       (WAR Module)      |
                       +------------+------------+
                                    |
          +-------------------------+-------------------------+
          | compiles & packages                               | compiles & packages
          v                                                   v
+-------------------------+                         +-------------------------+
|  MainetServiceAccount   |                         |MainetServiceMaterialMgmt|
|       (JAR Module)      |                         |       (JAR Module)      |
+------------+------------+                         +------------+------------+
             |                                                   |
             +-------------------------+-------------------------+
                                       | depends on
                                       v
                           +-------------------------+
                           |   MainetServiceCommon   |
                           |       (JAR Module)      |
                           +-------------------------+
```

### Module Characteristics
| Module | Packaging | Role & Contents |
| :--- | :--- | :--- |
| **`MainetServiceWeb`** | `war` | Servlet container deployment descriptors (`web.xml`), Spring application context XMLs, filter chains, static UI assets, JSPs, and WSDL service descriptors. |
| **`MainetServiceAccount`** | `jar` | Financial accounting Spring `@RestController`s, service layer implementations, JPA repositories, and DTOs. |
| **`MainetServiceMaterialMgmt`** | `jar` | Material and inventory Spring `@RestController`s, services, and entity mappings. |
| **`MainetServiceCommon`** | `jar` | Shared domain models, base entity types, utilities, pagination helpers, and logging beans. |

---

## 3. Web Layer Details (`MainetServiceWeb`)

Detailed inspection of `MainetServiceWeb` reveals:
1. **Source Code (`src/main/java`)**:
   - `MainetServiceWeb` does **not** contain compiled Java source files.
   - It functions strictly as an integration assembly and web container packaging module.
2. **Web Content (`src/main/webapp`)**:
   - `WEB-INF/web.xml`: Defines servlet mapping for Spring's `DispatcherServlet` (`/rest/*`), filters, error pages, and session timeouts.
   - `WEB-INF/classes/`: Contains environment configuration files and Spring component scanning definitions.
   - Static resources: CSS, JavaScript utilities, images, and legacy JSP interface templates.
3. **Maven Assembly**:
   - At packaging time, Maven bundles `MainetServiceAccount.jar`, `MainetServiceMaterialMgmt.jar`, and `MainetServiceCommon.jar` into `WEB-INF/lib/` inside `MainetService.war`.

---

## 4. Request Invocation Pathway

When an HTTP request is made to an endpoint such as:
`POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames`

The execution flow proceeds as follows:

```
[Client / Gateway]
       │
       ▼  HTTP POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames
[WildFly Web Subsystem]
       │
       ▼  Dispatches to MainetService.war Context
[MainetRequestLoggingFilter] (Filter Chain in web.xml)
       │  Logs URI, method, and client IP
       ▼
[Spring DispatcherServlet] (Mapped to /rest/*)
       │  Scans registered controller beans in application context
       ▼
[AccountReceiptEntryRestController] (Located in MainetServiceAccount.jar)
       │  @RequestMapping("/AccountReceiptEntry")
       │  @PostMapping("/doGetPayeeNames")
       ▼
[AccountReceiptEntryServiceImpl]
       │  Executes business validation (e.g. validateOrgId)
       ▼
[AccountReceiptEntryDaoImpl]
       │  Executes query against PostgreSQL DataSource
       ▼
[Response DTO / HTTP Status returned to Client]
```

---

## 5. Summary of Separation of Concerns

- **No Code Redundancy**: Web descriptors and configurations are centralized in `MainetServiceWeb`.
- **Modular Domain Services**: Business rules and data access logic remain strictly partitioned within their respective domain modules (`Account` and `MaterialMgmt`).
- **Clean Extensibility**: Additional domain services can be plugged into the Web deployment without modifying existing controller source code.
