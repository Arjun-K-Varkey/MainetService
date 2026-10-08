# Maven Module & Dependency Hierarchy Analysis

## 1. Overview

This document presents the structural analysis of the Maven multi-module configuration for the extracted `MainetService` repository. The application consists of three mandatory core modules, one required shared supporting module, and container deployment resources.

---

## 2. Maven Hierarchy

```
MainetServiceParent (pom.xml at root)
  │
  ├── MainetServiceCommon (JAR)
  │     └── Shared domain entities, DTOs, base DAOs, and utilities
  │
  ├── MainetServiceAccount (JAR)
  │     ├── Depends on: MainetServiceCommon
  │     └── Exposes: Financial accounting REST endpoints & services
  │
  ├── MainetServiceMaterialMgmt (JAR)
  │     ├── Depends on: MainetServiceCommon
  │     └── Exposes: Material inventory REST endpoints & services
  │
  └── MainetServiceWeb (WAR)
        ├── Depends on: MainetServiceAccount (JAR)
        ├── Depends on: MainetServiceMaterialMgmt (JAR)
        ├── Depends on: MainetServiceCommon (JAR)
        └── Produces: MainetService.war (WildFly deployment artifact)
```

---

## 3. Module Details & Rationale

### 3.1 Root Parent POM (`pom.xml`)
- **Group ID**: `com.inn.mainet`
- **Artifact ID**: `MainetService`
- **Packaging**: `pom`
- **Java Version Target**: `1.8`
- **Role**:
  - Centralizes dependency version management (`<dependencyManagement>`) for Spring Framework (4.x), Hibernate (5.x), Jackson (2.x), CXF, and logging libraries.
  - Standardizes compiler plugins (`maven-compiler-plugin` targeting Java 1.8).
  - Explicitly declares submodules:
    ```xml
    <modules>
        <module>MainetServiceCommon</module>
        <module>MainetServiceAccount</module>
        <module>MainetServiceMaterialMgmt</module>
        <module>MainetServiceWeb</module>
    </modules>
    ```

### 3.2 Required Application Modules
1. **`MainetServiceAccount`**:
   - Mandatory financial accounting module.
   - Provides Spring controllers (e.g., `AccountReceiptEntryRestController`), DAOs, service beans, and Liquibase change logs (`liquibase_account`).
2. **`MainetServiceMaterialMgmt`**:
   - Mandatory material and store management module.
   - Implements item master, requisition, stock ledger, and inventory workflows.
3. **`MainetServiceWeb`**:
   - Mandatory web container assembly module (`war`).
   - Bundles `web.xml`, Spring servlet dispatchers, static assets, and packaging configurations.

### 3.3 Required Supporting Modules
1. **`MainetServiceCommon`**:
   - Shared library module required by both `MainetServiceAccount` and `MainetServiceMaterialMgmt`.
   - Contains core models, domain interfaces, audit logging utilities, and shared constants.
2. **`Configuration/`**:
   - Supplies WildFly module configuration (`module.xml`) and externalized runtime properties (`serviceConfiguration.properties`, `service-endpoints.properties`).

---

## 4. Exclusion Rationale for Unrelated Modules

The original SVN repository contained 18 additional modules (e.g., `MainetServiceWater`, `MainetServicePropertyTax`, `MainetServiceBirthDeath`, etc.).
Analysis of the `pom.xml` dependency declarations in `MainetServiceAccount` and `MainetServiceMaterialMgmt` confirmed:
- Neither module has any compile-time or runtime dependencies on the 18 other domain modules.
- Only `MainetServiceCommon` is referenced as an internal dependency.
- Excluding the unrelated domain modules achieves a clean, self-contained, and focused repository without unnecessary build bloat.

---

## 5. Build Environment & Validation Results

- **JDK Version**: Oracle JDK 1.8.0_241
- **Maven Version**: Apache Maven 3.6.3

### Reactor Validation Results
| Module | Packaging | Status | Details |
| :--- | :--- | :--- | :--- |
| **MainetServiceParent** | pom | **SUCCESS** | Multi-module reactor validated cleanly. |
| **MainetServiceCommon** | jar | **SUCCESS** | 2,095 source files compiled cleanly with Java 8. |
| **MainetServiceAccount** | jar | **SUCCESS** | Financial accounting controllers, services, and DAOs compiled cleanly. |
| **MainetServiceMaterialMgmt** | jar | **SUCCESS** | Material management controllers, services, and DAOs compiled cleanly. |
| **MainetServiceWeb** | war | **ENVIRONMENT DEPENDENCY** | Requires internal enterprise SNAPSHOT dependencies (AdditionalServices, MainetServiceBirthAndDeath, MainetServiceMRM, MainetServiceLandEstate, MainetServiceRTS) from organization internal repository/Nexus. Per migration rules, dependencies are preserved without source modification. |
