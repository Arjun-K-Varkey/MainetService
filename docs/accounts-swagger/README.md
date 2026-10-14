# MainetService Accounts REST Swagger annotations

## Scope
This branch adds Swagger 2 annotations to the existing 25 active unique Accounts REST operations:
- Receipt Entry: 9 endpoints
- Vendor Bill Approval: 12 endpoints
- Voucher Posting: 4 endpoints

The commented-out duplicate legacy implementation of `/AccountVoucher/doExtSysVoucherPosting` is not documented as an extra operation.

## Changes
- Adds `@Api`, `@ApiOperation`, and `@ApiResponses` annotations to the three existing Accounts REST controllers.
- Adds `io.swagger:swagger-annotations:1.5.10` to `MainetServiceAccount`.
- Adds the Springfox 2 Swagger generator dependency (`io.springfox:springfox-swagger2:2.6.1`) to `MainetServiceWeb`.
- Adds `AccountsSwaggerConfig` with a Docket group named `accounts-rest`.
- Registers the Swagger configuration package in the existing REST dispatcher context.

Controller request mappings, DTO types, validation, encryption/checksum logic, and business method bodies are not intentionally changed.

## Intended Swagger JSON endpoint
The REST DispatcherServlet is mapped to `/rest/*` by `MainetServiceWeb/src/main/webapp/WEB-INF/web.xml`. After a successful build and deployment, try:

`https://<host>:<port>/<context>/rest/v2/api-docs?group=accounts-rest`

For a local WildFly deployment whose HTTPS port is 8443 and web context is `MainetService`, the candidate URL is:

`https://localhost:8443/MainetService/rest/v2/api-docs?group=accounts-rest`

This URL is a configuration-based expectation, not runtime-verified. Confirm it after deploying the WAR. If the endpoint returns 404 or 500, check Swagger generator wiring and Spring MVC logs before importing.

## WSO2 APIM 4.7.0
1. Build the repository using its Java 8 and Maven setup.
2. Deploy the generated WAR and retrieve the JSON endpoint above.
3. Validate that the Swagger JSON contains the 25 expected paths and POST operations.
4. Import the JSON in WSO2 Publisher, configure the reachable MainetService backend endpoint and security, then deploy/publish the API.
5. Test each API using valid payloads from existing callers. Swagger annotations do not create sample payloads that necessarily satisfy business validation.

## Analytics
Swagger annotations and Springfox do not enable WSO2 Analytics. Analytics must be configured in WSO2 APIM using a supported analytics provider, then verified by generating test traffic and checking request count, response codes, and latency in the provider's dashboard. Do not report Analytics as implemented until these metrics are visible.

## Validation status
The source change has been prepared on a feature branch. A build and runtime smoke test have not been performed by this editing session; validate compilation/deployment in the project's Java 8 / WildFly environment before merging.
