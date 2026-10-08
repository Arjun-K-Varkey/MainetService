# Contributing Guidelines

## 1. Overview

These guidelines outline standards and recommendations for contributing to the `MainetService` repository.

---

## 2. Branching Strategy Recommendation

- **`main`**: Production-ready, stable codebase. Direct commits to `main` are restricted in production repositories.
- **`develop`**: Integration branch for ongoing sprint deliverables.
- **`feature/<feature-name>`**: Dedicated feature branches branched from `develop` and merged via Pull Request.
- **`hotfix/<fix-name>`**: Urgent patches branched from `main` and merged into both `main` and `develop`.

---

## 3. Development & Build Standards

1. **Java & Maven Compatibility**:
   - Ensure all code compiles cleanly under Java 8 (`1.8`).
   - Do not introduce dependencies without proper licensing review and pom.xml dependencyManagement coordination.
2. **Local Validation**:
   - Always run `mvn clean compile -DskipTests` prior to submitting changes.
   - Verify that your changes do not break downstream modules.
3. **No Sensitive Data**:
   - Review `git status` and `git diff` before committing to confirm no logs, credentials, or local configuration files are staged.
4. **Clean Commit Messages**:
   - Follow standard semantic commit guidelines (e.g., `feat: ...`, `fix: ...`, `docs: ...`).
