# MainetServiceWeb Legacy Dependency & Runtime Analysis

## 1. Executive Summary

This document presents a comprehensive technical analysis of the 22 dependencies declared in `MainetServiceWeb/pom.xml`, with particular scrutiny of the **18 unmigrated corporate SNAPSHOT dependencies**.

### Key Findings
1. **Zero Java Code in Web Module**: `MainetServiceWeb` contains **0** compiled Java classes (`src/main/java` has 0 `.java` files). It functions exclusively as a Java EE WAR packaging container and web descriptor bundle.
2. **Accounts & Stores Are 100% Decoupled from the 18 Modules**:
   - `MainetServiceAccount` depends solely on `MainetServiceCommon` (0 references to any of the 18 modules).
   - `MainetServiceMaterialMgmt` depends solely on `MainetServiceCommon` (0 references to any of the 18 modules).
   - Neither Account nor Material Management imports, invokes, or accesses any class or service from the 18 unmigrated modules.
3. **The 18 Dependencies Are Legacy Monolithic Aggregations**:
   - In the legacy SKDCL monolithic architecture, `MainetServiceWeb` aggregated all 20+ municipal department modules (Water, Property Tax, Solid Waste, Trade License, etc.) into a single monolithic WAR.
   - The 18 dependencies are **not** compile-time dependencies of the Accounts or Stores domain logic.
4. **Packaging Blocker**:
   - 5 of the 18 corporate snapshot dependencies (`AdditionalServices`, `MainetServiceBirthAndDeath`, `MainetServiceMRM`, `MainetServiceLandEstate`, `MainetServiceRTS`) do not exist in public Maven Central and are not pre-cached in local `~/.m2`.
   - Their presence in `MainetServiceWeb/pom.xml` halts clean packaging (`mvn clean package`) for developers lacking private corporate Nexus access.

---

## 2. Complete 18-Dependency Inventory & Classification

| # | Dependency (groupId:artifactId:version) | Role in Municipal Suite | Referenced by Accounts? | Referenced by Stores? | Present in ~/.m2? | Classification |
|---|---|---|:---:|:---:|:---:|:---:|
| 1 | `com.abm.mainet:MainetServiceWater:2.1.1-beta-0-SNAPSHOT` | Water Connection & Billing | No | No | Yes (pre-cached) | **RED** |
| 2 | `com.abm.mainet:MainetServiceMobile:2.1.1-beta-0-SNAPSHOT` | Citizen Mobile Services | No | No | Yes (pre-cached) | **RED** |
| 3 | `com.abm.mainet:MainetServiceCare:2.1.1-beta-0-SNAPSHOT` | Public Grievance / Care | No | No | Yes (pre-cached) | **RED** |
| 4 | `com.abm.mainet:MainetServiceRnL:2.1.1-beta-0-SNAPSHOT` | Rent & Lease Management | No | No | Yes (pre-cached) | **RED** |
| 5 | `com.abm.mainet:MainetServiceWorksManagement:2.1.1-beta-0-SNAPSHOT` | Engineering Works / Tenders | No | No | Yes (pre-cached) | **RED** |
| 6 | `com.abm.mainet:MainetServiceProperty:2.1.1-beta-0-SNAPSHOT` | Property Tax Assessment | No | No | Yes (pre-cached) | **RED** |
| 7 | `com.abm.mainet:MainetServiceAssetManagement:2.1.1-beta-0-SNAPSHOT` | Fixed Assets Tracking | No | No | Yes (pre-cached) | **RED** |
| 8 | `com.abm.mainet:MainetServiceSWM:2.1.1-beta-0-SNAPSHOT` | Solid Waste Management | No | No | Yes (pre-cached) | **RED** |
| 9 | `com.abm.mainet:MainetServiceTradeLicense:2.1.1-beta-0-SNAPSHOT` | Trade & Hawker Licensing | No | No | Yes (pre-cached) | **RED** |
| 10 | `com.abm.mainet:AdditionalServices:2.1.1-beta-0-SNAPSHOT` | Ancillary Municipal Services | No | No | **No (Missing)** | **RED** |
| 11 | `com.abm.mainet:MainetServiceVehicleManagement:2.1.1-beta-0-SNAPSHOT` | Fleet & Fuel Logbook | No | No | Yes (pre-cached) | **RED** |
| 12 | `com.abm.mainet:MainetServiceBirthAndDeath:2.1.1-beta-0-SNAPSHOT` | Birth & Death Registration | No | No | **No (Missing)** | **RED** |
| 13 | `com.abm.mainet:MainetServiceLegal:2.1.1-beta-0-SNAPSHOT` | Legal Court Cases | No | No | Yes (pre-cached) | **RED** |
| 14 | `com.abm.mainet:MainetServiceSocialSecurity:2.1.1-beta-0-SNAPSHOT` | Welfare / Social Security | No | No | Yes (pre-cached) | **RED** |
| 15 | `com.abm.mainet:MainetServiceMRM:2.1.1-beta-0-SNAPSHOT` | Marriage Registration | No | No | **No (Missing)** | **RED** |
| 16 | `com.abm.mainet:MainetServiceLandEstate:2.1.1-beta-0-SNAPSHOT` | Land Acquisition & Estate | No | No | **No (Missing)** | **RED** |
| 17 | `com.abm.mainet:MainetServiceCouncil:2.1.1-beta-0-SNAPSHOT` | Municipal Council Meetings | No | No | Yes (pre-cached) | **RED** |
| 18 | `com.abm.mainet:MainetServiceRTS:2.1.1-beta-0-SNAPSHOT` | Right to Services SLA | No | No | **No (Missing)** | **RED** |

---

## 3. Source Reference Analysis

A comprehensive grep across all source code in the repository reveals:
1. **`MainetServiceAccount/src`**: Exactly **0** references to packages `com.abm.mainet.water`, `.care`, `.additionalservices`, etc.
2. **`MainetServiceMaterialMgmt/src`**: Exactly **0** references to the 18 external module packages.
3. **`MainetServiceCommon/src`**: Contains shared dashboard data models (e.g. `BndApplicationDtlEntity`, `LicenseDataEntity`), but these are generic persistence entities and have **no compile or runtime dependency** on the business logic of the 18 external modules.
4. **`MainetServiceWeb/src`**: Contains no Java code. The references exist exclusively in configuration descriptors and static presentation assets (JSP templates, i18n label properties).

---

## 4. Spring Runtime Analysis

Spring Framework runtime descriptors in `MainetServiceWeb` currently reference the legacy modules in three places:

### 4.1 Root ApplicationContext (`web.xml`)
The `contextConfigLocation` parameter in `web.xml` declares:
```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>
    classpath:/META-INF/spring/applicationContext.xml
    classpath:/META-INF/spring/cxf-beans.xml
    classpath:/applicationContext-common.xml
    classpath:/applicationContext-account.xml 
    classpath:/applicationContext-material-mgmt.xml 
    classpath:/applicationContext-tradeLicense.xml
    classpath:/applicationContext-rts.xml
    classpath:/applicationContext-care.xml  
    classpath:/applicationContext-property.xml
    classpath:/applicationContext-water.xml
    ...
  </param-value>
</context-param>
```
*Impact*: If an XML file explicitly listed in `contextConfigLocation` is absent from the classpath, Spring's `ContextLoaderListener` fails at application startup with a `BeanDefinitionStoreException` / `FileNotFoundException`.

### 4.2 Web MVC Dispatcher for HTML (`WEB-INF/spring-webmvc.xml`)
This dispatcher imports module-level MVC definitions:
```xml
<import resource="classpath:/spring-webmvc-common.xml"/>
<import resource="classpath:/spring-webmvc-account.xml"/>
<import resource="classpath:/spring-webmvc-material-mgmt.xml"/>
<import resource="classpath:/spring-webmvc-water.xml"/>
...
```
*Impact*: Hard `<import>` tags similarly require the target XML to exist on the classpath.

### 4.3 REST API Dispatcher (`WEB-INF/spring-webmvc-rest.xml`)
This dispatcher maps `/rest/*` endpoints via component scanning:
```xml
<context:component-scan base-package="
    com.abm.mainet.common.rest.ui.controller,
    com.abm.mainet.account.rest.ui.controller,
    com.abm.mainet.water.rest.ui.controller,
    ..." />
```
*Impact*: Spring's `<context:component-scan>` is **tolerant** of missing packages. If `com.abm.mainet.water.rest.ui.controller` is not on the classpath, Spring simply scans 0 beans for that package without failing.

---

## 5. JSP & Web Resource Analysis

Inspection of `MainetServiceWeb/src/main/webapp/jsp` reveals 51 subdirectories:
- **In-Scope**:
  - `account/`: 293 JSP files (vouchers, receipt entry, ledgers, cheque realization)
  - `material_mgmt/`: 64 JSP files (item master, purchase requisition, inventory)
  - `common/`: 14 JSP files (base layouts, error views)
  - `tiles/`, `layouts/`, `eip/`: 50+ supporting template files
- **Out-of-Scope Legacy JSPs**:
  - `water/` (123 files), `property/` (210 files), `solid_waste_management/` (177 files), `works_management/` (141 files), `trade_license/` (80 files), `BirthAndDeath/` (62 files), etc.
- **Finding**: The legacy JSP pages are static files bundled in the WAR. They do not trigger build failures unless compiled during build (which Maven does not do by default for JSPs). However, they represent dead code in an Accounts + Stores scoped deployment.

---

## 6. Accounts Minimum Dependency Graph

`MainetServiceAccount` requires only:
```
MainetServiceAccount (JAR)
  └── MainetServiceCommon (JAR)
        ├── Spring Web / WebMVC (4.2.0.RELEASE)
        ├── Spring Data JPA / ORM (1.9.0.RELEASE)
        ├── Hibernate Core (5.0.1.Final)
        ├── Jackson Core & Databind (2.4.3)
        ├── Apache CXF (3.2.6)
        ├── Log4j & SLF4J
        └── PostgreSQL / JDBC DataSource
```
**Conclusion**: `MainetServiceAccount` has **zero** dependency on the 18 external modules.

---

## 7. Stores Minimum Dependency Graph

`MainetServiceMaterialMgmt` requires only:
```
MainetServiceMaterialMgmt (JAR)
  └── MainetServiceCommon (JAR)
        ├── Spring Web / WebMVC (4.2.0.RELEASE)
        ├── Spring Data JPA / ORM (1.9.0.RELEASE)
        ├── Hibernate Core (5.0.1.Final)
        ├── Jackson Core & Databind (2.4.3)
        ├── Apache CXF (3.2.6)
        └── PostgreSQL / JDBC DataSource
```
**Conclusion**: `MainetServiceMaterialMgmt` has **zero** dependency on the 18 external modules.

---

## 8. Configuration Path Analysis

Investigation of the 7 occurrences of `D:\Documents\SKDCL` in the repository confirms:
- **Source Code**: **0 occurrences**. No Java code contains hardcoded developer file paths.
- **Runtime Configuration**: **0 occurrences**. `service-classes-configuration.properties` contains logical class name mappings (e.g. `ARE=com.abm.mainet.account.service.AccountReceiptEntryServiceImpl`) without filesystem paths.
- **Documentation & Evidence**: **7 occurrences**:
  - `apisix-poc/docs/APISIX_MainetService_POC_Phase2A_RequestID_Evidence.md` (historical POC path note)
  - `docs/migration/FINAL_BUILD_VERIFICATION.md` (source verification record)
  - `docs/migration/MIGRATION_REPORT.md` (SVN origin audit record)
  - `docs/migration/SVN_TO_GITHUB.md` (migration strategy record)

**Finding**: The hardcoded path string does **not** affect runtime or application behavior in any way.

---

## 9. WildFly Module Analysis

`MainetServiceWeb` requires a single custom WildFly server module:
- **Module Name**: `com.abm.configuration.mainetService`
- **Deployment Descriptor**: `MainetServiceWeb/src/main/webapp/WEB-INF/jboss-deployment-structure.xml`
- **Module Configuration**: `Configuration/com/abm/configuration/mainetService/main/module.xml`
- **Expected Resources**:
  - `serviceConfiguration.properties` (datasource, security, and logging parameters)
  - `service-endpoints.properties` (external web service endpoint URLs)
  - `service-classes-configuration.properties` (LOI and payment class dispatchers)
- **Runtime Purpose**: Externalizes environment-specific configuration outside the WAR deployment, allowing server admins to configure database credentials and endpoints without rebuilding the application artifact.

---

## 10. Minimum Required WAR Dependency Set

To package a functional, buildable `MainetService.war` providing Accounts and Stores REST APIs and UI, the required dependency set in `MainetServiceWeb/pom.xml` is:

```xml
<dependencies>
    <!-- 1. Shared Base Library (A: In Git Repo) -->
    <dependency>
        <groupId>com.abm.mainet</groupId>
        <artifactId>MainetServiceCommon</artifactId>
        <version>${mainet.service.common.version}</version>
    </dependency>

    <!-- 2. Financial Accounting Domain (A: In Git Repo) -->
    <dependency>
        <groupId>com.abm.mainet</groupId>
        <artifactId>MainetServiceAccount</artifactId>
        <version>${mainet.service.account.version}</version>
    </dependency>

    <!-- 3. Material & Inventory Domain (A: In Git Repo) -->
    <dependency>
        <groupId>com.abm.mainet</groupId>
        <artifactId>MainetServiceMaterialMgmt</artifactId>
        <version>${mainet.service.materialmgmt.version}</version>
    </dependency>

    <!-- 4. Container Servlet API (B: External Public / Provided) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 11. Dependencies That Can Potentially Be Removed

The following 18 dependencies are completely unneeded for the Accounts + Material Management service scope:
1. `com.abm.mainet:MainetServiceWater`
2. `com.abm.mainet:MainetServiceMobile`
3. `com.abm.mainet:MainetServiceCare`
4. `com.abm.mainet:MainetServiceRnL`
5. `com.abm.mainet:MainetServiceWorksManagement`
6. `com.abm.mainet:MainetServiceProperty`
7. `com.abm.mainet:MainetServiceAssetManagement`
8. `com.abm.mainet:MainetServiceSWM`
9. `com.abm.mainet:MainetServiceTradeLicense`
10. `com.abm.mainet:AdditionalServices`
11. `com.abm.mainet:MainetServiceVehicleManagement`
12. `com.abm.mainet:MainetServiceBirthAndDeath`
13. `com.abm.mainet:MainetServiceLegal`
14. `com.abm.mainet:MainetServiceSocialSecurity`
15. `com.abm.mainet:MainetServiceMRM`
16. `com.abm.mainet:MainetServiceLandEstate`
17. `com.abm.mainet:MainetServiceCouncil`
18. `com.abm.mainet:MainetServiceRTS`

---

## 12. Dependencies That MUST Remain

The following dependencies are **strictly mandatory**:
1. `com.abm.mainet:MainetServiceCommon` (supplies core entities, base DAOs, exception handlers, JSON mappers)
2. `com.abm.mainet:MainetServiceAccount` (supplies financial accounting controllers, services, DAOs, DTOs)
3. `com.abm.mainet:MainetServiceMaterialMgmt` (supplies store inventory controllers, services, DAOs)
4. `javax.servlet:javax.servlet-api` (container compilation dependency)
5. Inherited parent dependencies from `pom.xml` (Spring 4.2, Hibernate 5.0, CXF 3.2.6, Jackson, etc.)

---

## 13. Risks

1. **Risk of Retaining the 18 Dependencies**:
   - `mvn clean package` will continue to fail in any environment that lacks access to the corporate Nexus server hosting snapshot JARs of `AdditionalServices`, `BirthAndDeath`, `MRM`, `LandEstate`, and `RTS`.
2. **Risk of Removing Dependencies from POM Without Adjusting `web.xml`**:
   - If the 18 dependencies are removed from `MainetServiceWeb/pom.xml`, Maven package will succeed immediately and generate `MainetService.war`.
   - **However**, at runtime on WildFly, Spring's `ContextLoaderListener` will attempt to load the XML context files listed in `web.xml` (`classpath:/applicationContext-water.xml`, etc.) and `spring-webmvc.xml` (`classpath:/spring-webmvc-water.xml`), resulting in a startup failure (`FileNotFoundException`).
3. **Coordinated Decoupling Requirement**:
   - Truly decoupling `MainetServiceWeb` requires removing the 18 dependencies from `pom.xml` **in tandem with** removing the corresponding non-Accounts XML lines from `web.xml` and `spring-webmvc.xml`.

---

## 14. Recommended POM Strategy

### Strategy Evaluation
- **Option 1 (Corporate Nexus Configuration)**:
  Configure private Nexus server credentials in Maven settings.
  *Assessment*: Solves the packaging error without changing files, but maintains monolithic baggage (packaging 18 irrelevant modules into a WAR when only Accounts and Stores are active).
- **Option 2 (Scoped Modular Packaging — RECOMMENDED)**:
  Align `MainetServiceWeb` with the repository's stated scope:
  1. Restrict `MainetServiceWeb/pom.xml` to `MainetServiceCommon`, `MainetServiceAccount`, `MainetServiceMaterialMgmt`, and `javax.servlet-api`.
  2. Comment out or remove the 18 unmigrated `<import>` statements from `web.xml` and `spring-webmvc.xml`.
  3. Leave `spring-webmvc-rest.xml` untouched (its component scanner safely ignores absent packages).
  *Assessment*: Produces a clean, lightweight, 100% self-contained WAR that builds and runs independently on any standard Java 8 + WildFly 10 environment with zero external corporate dependencies.

---

## 15. Final Classification Summary

- **`MainetServiceCommon`**: **GREEN** (Required, present in Git repo)
- **`MainetServiceAccount`**: **GREEN** (Required, present in Git repo)
- **`MainetServiceMaterialMgmt`**: **GREEN** (Required, present in Git repo)
- **`javax.servlet-api`**: **GREEN** (Required, available in Maven Central)
- **18 Corporate SNAPSHOT Dependencies**: **RED** (Completely unnecessary for Accounts/Stores; halts standalone packaging)
