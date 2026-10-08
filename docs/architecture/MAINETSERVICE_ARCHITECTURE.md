# MainetService Application Architecture

## 1. Executive Overview

MainetService is an enterprise municipal service platform built on Java EE 7 and Spring Framework, deployed on JBoss WildFly Application Server (version 10.1.0.Final). The application provides modular RESTful and SOAP services supporting core urban governance functions, notably **Financial Accounting (MainetServiceAccount)** and **Material & Inventory Management (MainetServiceMaterialMgmt)**.

This document outlines the architectural layers, module relationships, runtime deployment model, and API Gateway integration layers as verified directly from the source code.

---

## 2. Architectural Layers

`
                     +---------------------------------------+
                     |         Clients & Gateways            |
                     |  (APISIX Gateway / WSO2 APIM / Web UI)|
                     +-------------------+-------------------+
                                         | HTTP / REST (JSON)
                                         v
                     +---------------------------------------+
                     |           MainetServiceWeb            |
                     |  (WAR Container / web.xml / Spring)   |
                     +-------------------+-------------------+
                                         | DispatcherServlet (/rest/*)
                                         v
                     +---------------------------------------+
                     |         REST Controller Layer         |
                     |  (AccountReceiptEntry, MaterialMaster)|
                     +-------------------+-------------------+
                                         | Service Interface Invocations
                                         v
                     +---------------------------------------+
                     |          Service Business Layer       |
                     |  (Spring Services, Business Rules)    |
                     +-------------------+-------------------+
                                         | Data Access
                                         v
                     +---------------------------------------+
                     |          DAO & Persistence Layer      |
                     |      (Hibernate / JPA / JDBC)         |
                     +-------------------+-------------------+
                                         | Shared Utilities & Models
                                         v
                     +---------------------------------------+
                     |          MainetServiceCommon          |
                     |    (Base Entities, DTOs, Helpers)     |
                     +-------------------+-------------------+
                                         | JDBC DataSource
                                         v
                     +---------------------------------------+
                     |      WildFly 10 Application Server    |
                     |   (JNDI DataSources, Logging Modules) |
                     +-------------------+-------------------+
                                         | SQL / Liquibase
                                         v
                     +---------------------------------------+
                     |          PostgreSQL Database          |
                     +---------------------------------------+
`

### 2.1 Presentation & Web Layer (MainetServiceWeb)
- **Package Type**: WAR (MainetService.war).
- **Configuration**: Standard Java EE web.xml and Spring MVC web-application-config.xml.
- **Role**:
  - Hosts the servlet filters (such as MainetRequestLoggingFilter, CORS filters, and authentication handlers).
  - Routes inbound HTTP requests to Spring's DispatcherServlet under /rest/*.
  - Packages and bundles static web resources, JSP pages, and WSDL contracts.
  - Combines modular backend JARs (MainetServiceAccount, MainetServiceMaterialMgmt, MainetServiceCommon) into the final deployable artifact.

### 2.2 Domain Business Modules
- **MainetServiceAccount**:
  - Implements financial accounting operations including voucher generation, receipt entry (AccountReceiptEntryRestController), ledger postings, and collection processing.
  - Implements transaction demarcation via Spring @Transactional annotations.
- **MainetServiceMaterialMgmt**:
  - Implements material procurement, store inventory tracking, item master cataloging, purchase requisition workflows, and stock adjustments.
- **MainetServiceCommon**:
  - Shared domain models, base entity definitions, pagination utilities, common exception handlers, date/number formatting, and shared logging helpers.

### 2.3 Runtime Container (WildFly 10.1.0)
- Configured with standalone server profile (standalone.xml).
- Supplies container-managed resources:
  - Managed JDBC DataSources (e.g., java:/MainetDS) providing connection pooling.
  - Logging subsystem with application-specific log categories (com.inn.mainet).
  - WildFly custom module com.inn.mainet.service.configuration loading externalized configuration (serviceConfiguration.properties, service-endpoints.properties).

### 2.4 Database & Schema Migration
- Primary datastore: PostgreSQL.
- Database version control and schema migrations managed using Liquibase XML/properties changesets located under liquibase_account/, liquibase_common/, and liquibase_web/.

---

## 3. API Management Layer (POC Gateways)

The MainetService architecture supports enterprise API Gateway mediation through two evaluated solutions:

1. **Apache APISIX**:
   - High-performance OpenResty/Nginx-based gateway.
   - Evaluated for dynamic upstream routing, correlation ID injection (
equest-id), and rate limiting.
   - Routes requests directly to the WildFly HTTP endpoint (http://localhost:8080/MainetService/rest/*).

2. **WSO2 API Manager 4.x**:
   - Enterprise API lifecycle platform with API Publisher, Developer Portal, and Synapse Gateway.
   - Enforces OAuth 2.0 access control, client application subscriptions, token validation via Key Manager, and API analytics.
   - Proxies consumer requests securely to backend WildFly REST endpoints.
