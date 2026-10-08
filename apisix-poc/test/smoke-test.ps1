<#
.SYNOPSIS
    Smoke-test script for MainetService APISIX Phase 1 POC.
.DESCRIPTION
    Executes four targeted HTTP requests:
      A. Direct Spring MVC API (WildFly :8080)
      B. APISIX Spring MVC API (APISIX :9080)
      C. Direct Apache CXF API (WildFly :8080)
      D. APISIX Apache CXF API (APISIX :9080)

    Reports HTTP status, response snippet, and Pass/Fail status for each.
#>

param(
    [string]$WildFlyHost = "localhost:8080",
    [string]$ApisixHost  = "localhost:9080"
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$accountsPayloadPath = Join-Path $scriptDir "test-payload-accounts.json"

$accountsPayload = if (Test-Path $accountsPayloadPath) { Get-Content $accountsPayloadPath -Raw } else { '{"orgId": 1}' }

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Apache APISIX Phase 1 Smoke Test - MainetService                " -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " Target WildFly Host : $WildFlyHost"
Write-Host " Target APISIX Host  : $ApisixHost"
Write-Host ""

$results = @()

function Invoke-ApiTest {
    param(
        [string]$TestId,
        [string]$TestName,
        [string]$Url,
        [string]$Method,
        [string]$Body = $null
    )

    Write-Host "-----------------------------------------------------------------" -ForegroundColor Gray
    Write-Host "[$TestId] $TestName" -ForegroundColor Yellow
    Write-Host "URL    : $Url" -ForegroundColor Gray
    Write-Host "Method : $Method" -ForegroundColor Gray
    if ($Body) {
        Write-Host "Payload: $Body" -ForegroundColor Gray
    }

    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    $statusCode = 0
    $responseBody = ""
    $isSuccess = $false

    try {
        $params = @{
            Uri             = $Url
            Method          = $Method
            TimeoutSec      = 15
            UseBasicParsing = $true
            ErrorAction     = "Stop"
        }
        if ($Body) {
            $params["Body"] = $Body
            $params["ContentType"] = "application/json"
        }
        $response = Invoke-WebRequest @params
        $stopwatch.Stop()
        $statusCode = [int]$response.StatusCode
        $responseBody = $response.Content
        if ($statusCode -ge 200 -and $statusCode -lt 300) {
            $isSuccess = $true
        }
    } catch [System.Net.WebException] {
        $stopwatch.Stop()
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
        } else {
            $responseBody = $_.Exception.Message
        }
    } catch {
        $stopwatch.Stop()
        $responseBody = $_.Exception.Message
    }

    $snippet = if ($responseBody.Length -gt 250) { $responseBody.Substring(0, 250) + "..." } else { $responseBody }

    Write-Host "HTTP Status : $statusCode" -ForegroundColor (if ($isSuccess) { "Green" } else { "Red" })
    Write-Host "Elapsed     : $($stopwatch.ElapsedMilliseconds) ms" -ForegroundColor Gray
    Write-Host "Response    : $snippet" -ForegroundColor Gray
    Write-Host "Result      : $(if ($isSuccess) { 'PASS' } else { 'FAIL' })" -ForegroundColor (if ($isSuccess) { "Green" } else { "Red" })

    return [PSCustomObject]@{
        TestId      = $TestId
        Name        = $TestName
        Url         = $Url
        Status      = $statusCode
        DurationMs  = $stopwatch.ElapsedMilliseconds
        Result      = if ($isSuccess) { "PASS" } else { "FAIL" }
        RawResponse = $responseBody
    }
}

# Test A: Direct Spring MVC API
$testA = Invoke-ApiTest -TestId "A" `
    -TestName "Direct Spring MVC REST (WildFly :8080)" `
    -Url "http://$WildFlyHost/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
    -Method "POST" `
    -Body $accountsPayload
$results += $testA

# Test B: APISIX Spring MVC API
$testB = Invoke-ApiTest -TestId "B" `
    -TestName "APISIX Spring MVC REST (APISIX :9080)" `
    -Url "http://$ApisixHost/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" `
    -Method "POST" `
    -Body $accountsPayload
$results += $testB

# Test C: Direct CXF API
$testC = Invoke-ApiTest -TestId "C" `
    -TestName "Direct Apache CXF REST (WildFly :8080)" `
    -Url "http://$WildFlyHost/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" `
    -Method "GET"
$results += $testC

# Test D: APISIX CXF API
$testD = Invoke-ApiTest -TestId "D" `
    -TestName "APISIX Apache CXF REST (APISIX :9080)" `
    -Url "http://$ApisixHost/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" `
    -Method "GET"
$results += $testD

Write-Host "=================================================================" -ForegroundColor Cyan
Write-Host " SUMMARY MATRIX                                                  " -ForegroundColor Cyan
Write-Host "=================================================================" -ForegroundColor Cyan
$results | Select-Object TestId, Name, Status, DurationMs, Result | Format-Table -AutoSize

Write-Host ""
Write-Host "Response Parity Check:" -ForegroundColor Yellow
if ($testA.Status -eq $testB.Status -and $testA.Status -gt 0) {
    Write-Host " - Spring MVC: Direct Status ($($testA.Status)) matches APISIX Status ($($testB.Status))" -ForegroundColor Green
} else {
    Write-Host " - Spring MVC: Status mismatch or connection failure (Direct: $($testA.Status), APISIX: $($testB.Status))" -ForegroundColor Yellow
}

if ($testC.Status -eq $testD.Status -and $testC.Status -gt 0) {
    Write-Host " - CXF REST  : Direct Status ($($testC.Status)) matches APISIX Status ($($testD.Status))" -ForegroundColor Green
} else {
    Write-Host " - CXF REST  : Status mismatch or connection failure (Direct: $($testC.Status), APISIX: $($testD.Status))" -ForegroundColor Yellow
}
Write-Host "=================================================================" -ForegroundColor Cyan
