#!/usr/bin/env bash
# ==============================================================================
# Smoke-test script for MainetService APISIX Phase 1 POC
# ==============================================================================

WILDFLY_HOST="${1:-localhost:8080}"
APISIX_HOST="${2:-localhost:9080}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ACCOUNTS_PAYLOAD="${SCRIPT_DIR}/test-payload-accounts.json"

echo "================================================================="
echo " Apache APISIX Phase 1 Smoke Test - MainetService                "
echo "================================================================="
echo " Target WildFly Host : $WILDFLY_HOST"
echo " Target APISIX Host  : $APISIX_HOST"
echo ""

run_test() {
  local test_id="$1"
  local test_name="$2"
  local url="$3"
  local method="$4"
  local payload_file="$5"

  echo "-----------------------------------------------------------------"
  echo "[$test_id] $test_name"
  echo "URL    : $url"
  echo "Method : $method"

  local response_file
  response_file=$(mktemp)
  local http_code

  if [[ "$method" == "POST" && -f "$payload_file" ]]; then
    echo "Payload: $(cat "$payload_file")"
    http_code=$(curl -s -w "%{http_code}" -o "$response_file" \
      -X POST "$url" \
      -H "Content-Type: application/json" \
      -d @"$payload_file" \
      --connect-timeout 10 \
      --max-time 30 || echo "000")
  else
    http_code=$(curl -s -w "%{http_code}" -o "$response_file" \
      -X GET "$url" \
      --connect-timeout 10 \
      --max-time 30 || echo "000")
  fi

  local snippet
  snippet=$(head -c 250 "$response_file" || true)
  rm -f "$response_file"

  echo "HTTP Status : $http_code"
  echo "Response    : $snippet"

  if [[ "$http_code" =~ ^2[0-9][0-9]$ ]]; then
    echo "Result      : PASS"
    return 0
  else
    echo "Result      : FAIL"
    return 1
  fi
}

echo ""
run_test "A" "Direct Spring MVC REST (WildFly :8080)" \
  "http://${WILDFLY_HOST}/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
  "POST" \
  "$ACCOUNTS_PAYLOAD" || true

echo ""
run_test "B" "APISIX Spring MVC REST (APISIX :9080)" \
  "http://${APISIX_HOST}/MainetService/rest/AccountReceiptEntry/doGetPayeeNames" \
  "POST" \
  "$ACCOUNTS_PAYLOAD" || true

echo ""
run_test "C" "Direct Apache CXF REST (WildFly :8080)" \
  "http://${WILDFLY_HOST}/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" \
  "GET" || true

echo ""
run_test "D" "APISIX Apache CXF REST (APISIX :9080)" \
  "http://${APISIX_HOST}/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount" \
  "GET" || true

echo "================================================================="
echo " Smoke test run completed."
echo "================================================================="
