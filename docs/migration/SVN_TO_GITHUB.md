# SVN to GitHub Migration Strategy & Record

## 1. Context & Background

The MainetService platform and associated components were historically maintained in a monolithic Apache Subversion (SVN) repository workspace at `D:\Documents\SKDCL`.

As part of the modernization and API enablement roadmap, this repository was established to extract the required MainetService domain modules and API Gateway POCs into an independent, version-controlled GitHub repository.

---

## 2. Core Migration Principles

1. **Original Source Untouched (Source of Truth)**:
   - The original SKDCL SVN workspace (`D:\Documents\SKDCL`) remains the immutable source of truth.
   - Zero files were renamed, deleted, moved, modified, or cleaned in the original SVN working copy.
   - No Git operations were performed inside the SVN tree.
2. **Clean Git History**:
   - SVN history and revision metadata (`.svn` directories) were intentionally excluded.
   - The GitHub repository starts with a clean, well-defined baseline commit on the `main` branch.
3. **No Code Modernization / Refactoring**:
   - The application source code, package structures, Java classes, JSPs, CSS, JS, and Maven dependencies were imported verbatim.
   - No framework upgrades or structural alterations were applied.
4. **Hard Security Gate**:
   - All legacy credentials, cleartext database passwords, and API gateway keys found in configuration files were sanitized before Git tracking.
   - Reusable `.example` configuration templates were established.

---

## 3. Scope of Extracted Modules

| Component | Source SVN Path | Target Repository Path | Status |
| :--- | :--- | :--- | :--- |
| **Root Maven POM** | `MainetService/pom.xml` | `pom.xml` | Extracted & Sanitized |
| **WildFly Config Module** | `MainetService/Configuration/` | `Configuration/` | Extracted & Sanitized |
| **Common Shared Library** | `MainetService/MainetServiceCommon/` | `MainetServiceCommon/` | Extracted (Verbatim) |
| **Account Module** | `MainetService/MainetServiceAccount/` | `MainetServiceAccount/` | Extracted (Verbatim) |
| **Material Management** | `MainetService/MainetServiceMaterialMgmt/`| `MainetServiceMaterialMgmt/` | Extracted (Verbatim) |
| **Web Container Module** | `MainetService/MainetServiceWeb/` | `MainetServiceWeb/` | Extracted (Verbatim) |
| **APISIX Gateway POC** | `MainetService/apisix-poc/` | `apisix-poc/` | Extracted & Sanitized |
| **WSO2 APIM Gateway POC** | `WSO2-APIM-POC/` | `wso2-apim-poc/` | Extracted & Sanitized |

---

## 4. Future Development & Maintenance

Moving forward, the `MainetService` GitHub repository serves as the central collaboration and CI/CD target for the extracted service modules. Developers should clone directly from GitHub, use feature branching, and rely on `.env.example` / `*.example` templates for local environment configuration.
