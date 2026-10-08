# MainetService

Enterprise municipal service management platform providing modular RESTful and SOAP services for urban governance, accounting, and material management.

---

## 1. Application Overview

MainetService is built with Java EE 7 and Spring Framework, running on WildFly 10 Application Server with PostgreSQL. This repository contains the core enterprise financial accounting and inventory management modules, the web packaging layer, and API Gateway Proof of Concept (POC) configurations for Apache APISIX and WSO2 API Manager.

---

## 2. Repository Structure

```
MainetService/
├── MainetServiceAccount/          # Financial accounting REST controllers, services, DAOs
├── MainetServiceMaterialMgmt/     # Material procurement & inventory management module
├── MainetServiceWeb/              # WAR packaging, web.xml, Spring DispatcherServlet, static assets
├── MainetServiceCommon/           # Shared models, DTOs, base persistence, utilities
├── Configuration/                 # WildFly runtime module descriptors and property templates
├── apisix-poc/                    # Apache APISIX API Gateway POC (Docker, routes, scripts)
├── wso2-apim-poc/                 # WSO2 API Manager 4.x POC (Docker, deployment config, docs)
├── docs/
│   ├── architecture/              # Architecture and web-to-backend relationship documentation
│   ├── migration/                 # SVN-to-GitHub and Maven dependency analysis documentation
│   └── poc/                       # API gateway POC status and validation records
├── pom.xml                        # Multi-module root Maven POM
├── .gitignore                     # Git ignore rules for Java, Maven, IDEs, and environments
├── SECURITY.md                    # Security policy, credential management guidelines
└── CONTRIBUTING.md                # Development, branching, and code contribution standards
```

---

## 3. Application Modules

| Module | Type | Description |
| :--- | :--- | :--- |
| **`MainetServiceAccount`** | JAR | Accounting receipts, vouchers, ledger processing, and collections. |
| **`MainetServiceMaterialMgmt`** | JAR | Store inventory, item catalog, purchase requisitions, and stock tracking. |
| **`MainetServiceWeb`** | WAR | Integration assembly, servlet filters, Spring MVC routing (`/rest/*`). |
| **`MainetServiceCommon`** | JAR | Shared domain base classes, utilities, and cross-cutting helpers. |

---

## 4. Build & Compilation

### Prerequisites
- **JDK**: Java SE Development Kit 8 (1.8.0_241 or compatible)
- **Maven**: Apache Maven 3.6.0+

### Build Commands
```bash
# Verify POM configuration
mvn validate

# Compile all modules without running unit tests
mvn clean compile -DskipTests

# Build the complete deployable WAR artifact
mvn clean package -DskipTests
```
The deployable archive is generated at `MainetServiceWeb/target/MainetService.war`.

---

## 5. API Gateway POCs

### Apache APISIX POC (`apisix-poc/`)
Evaluates lightweight, high-performance API routing and request correlation ID injection.
- Start APISIX:
  ```bash
  cd apisix-poc
  docker-compose up -d
  ```
- Initialize Routes:
  ```bash
  pwsh ./scripts/init-routes.ps1
  ```

### WSO2 API Manager POC (`wso2-apim-poc/`)
Evaluates enterprise API lifecycle governance, OAuth 2.0 security, developer portal subscription, and API rate limiting.
- Details and deployment instructions are provided in `wso2-apim-poc/README.md`.

---

## 6. Configuration & Security

- **Do Not Commit Secrets**: Never commit passwords, tokens, private keys, or actual credentials.
- **Configuration Templates**: Use provided `.example` files (e.g., `config.yaml.example`, `deployment.toml.example`, `serviceConfiguration.properties.example`) to configure local environments.
- For security disclosure policies, review [SECURITY.md](SECURITY.md).
