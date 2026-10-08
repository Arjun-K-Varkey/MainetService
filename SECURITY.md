# Security Policy

## 1. Overview & Commitment

Security is a foundational requirement for MainetService. This policy outlines security standards for managing credentials, environment configurations, and vulnerability reporting.

---

## 2. Hard Security Rules

1. **Zero Cleartext Credentials in Source Control**:
   - Never commit passwords, private keys, API keys, OAuth client secrets, or bearer tokens.
   - Never commit database credentials or connection strings containing passwords.
   - Any commit violating this policy will be rejected or purged immediately.
2. **Environment Configuration via Placeholders**:
   - Maintain configuration templates using the `.example` suffix (e.g., `application.properties.example`, `deployment.toml.example`).
   - Actual runtime values must be injected via environment variables, container secrets, or secure configuration modules.
3. **Key and Certificate Security**:
   - Keystores (`.jks`, `.p12`), private keys (`.key`), and certificates (`.pem`, `.crt`) must remain outside git tracking.

---

## 3. Secret Rotation & Remediation

If credentials or sensitive data are accidentally exposed:
1. Immediately revoke and rotate the compromised credential across all systems.
2. Rewrite repository history if necessary to eliminate references to the secret.
3. File an internal security incident report to verify whether unauthorized access occurred.

---

## 4. Reporting Security Vulnerabilities

To report a vulnerability or potential security risk in MainetService:
- Do not open a public issue.
- Contact the designated engineering security lead or repository administrator directly.
- Provide a detailed description, reproduction steps, and potential impact.
