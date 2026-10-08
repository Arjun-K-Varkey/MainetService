<#
.SYNOPSIS
    Provisions Phase 1 APISIX routes for MainetService via Admin API.
.DESCRIPTION
    Configures two routes targeting the local WildFly instance:
    1. Spring MVC REST: POST /MainetService/rest/AccountReceiptEntry/doGetPayeeNames
    2. Apache CXF REST: GET /MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount
#>

$adminUrl = "http://localhost:9180/apisix/admin"
$apiKey   = "YOUR_APISIX_ADMIN_KEY"
$headers  = @{
    "X-API-KEY"    = $apiKey
    "Content-Type" = "application/json"
}

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host " Provisioning APISIX Phase 1 Routes for MainetService       " -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan

# Route 1: Spring MVC REST (Accounts Payee Names)
$route1Id = "mainetservice-accounts-get-payee-names"
$route1Body = @{
    name     = "mainetservice-accounts-get-payee-names"
    desc     = "POC Route 1: Spring MVC REST - Accounts Payee Names Lookup"
    uri      = "/MainetService/rest/AccountReceiptEntry/doGetPayeeNames"
    methods  = @("POST")
    upstream = @{
        type      = "roundrobin"
        nodes     = @{ "host.docker.internal:8080" = 1 }
        timeout   = @{ connect = 6; send = 6; read = 60 }
        pass_host = "pass"
    }
} | ConvertTo-Json -Depth 5

Write-Host "Creating Route 1: $route1Id ..." -NoNewline
try {
    $res1 = Invoke-RestMethod -Uri "$adminUrl/routes/$route1Id" -Method Put -Headers $headers -Body $route1Body
    Write-Host " [SUCCESS]" -ForegroundColor Green
} catch {
    Write-Host " [FAILED]: $_" -ForegroundColor Red
}

# Route 2: Apache CXF REST (License Dashboard Year-wise Count)
$route2Id = "mainetservice-common-license-yearwise-count"
$route2Body = @{
    name     = "mainetservice-common-license-yearwise-count"
    desc     = "POC Route 2: Apache CXF REST - Year-wise License Count Statistics"
    uri      = "/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount"
    methods  = @("GET")
    upstream = @{
        type      = "roundrobin"
        nodes     = @{ "host.docker.internal:8080" = 1 }
        timeout   = @{ connect = 6; send = 6; read = 60 }
        pass_host = "pass"
    }
} | ConvertTo-Json -Depth 5

Write-Host "Creating Route 2: $route2Id ..." -NoNewline
try {
    $res2 = Invoke-RestMethod -Uri "$adminUrl/routes/$route2Id" -Method Put -Headers $headers -Body $route2Body
    Write-Host " [SUCCESS]" -ForegroundColor Green
} catch {
    Write-Host " [FAILED]: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "Verification: Listing Active Routes..." -ForegroundColor Yellow
try {
    $routes = Invoke-RestMethod -Uri "$adminUrl/routes" -Method Get -Headers $headers
    $count = $routes.list.Count
    Write-Host "Active route count: $count" -ForegroundColor Green
    foreach ($item in $routes.list) {
        Write-Host " - Route ID: $($item.value.id) | URI: $($item.value.uri) | Methods: $($item.value.methods -join ', ')" -ForegroundColor Gray
    }
} catch {
    Write-Host "Could not query routes: $_" -ForegroundColor Red
}
