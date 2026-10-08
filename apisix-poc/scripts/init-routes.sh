#!/usr/bin/env bash
# ==============================================================================
# Provision Phase 1 APISIX Routes for MainetService via Admin API
# ==============================================================================

set -euo pipefail

ADMIN_URL="http://localhost:9180/apisix/admin"
API_KEY="YOUR_APISIX_ADMIN_KEY"

echo "============================================================"
echo " Provisioning APISIX Phase 1 Routes for MainetService       "
echo "============================================================"

# Route 1: Spring MVC REST (Accounts Payee Names)
ROUTE1_ID="mainetservice-accounts-get-payee-names"
echo -n "Configuring Route 1 ($ROUTE1_ID)... "
curl -s -o /dev/null -w "%{http_code}\n" -X PUT "$ADMIN_URL/routes/$ROUTE1_ID" \
  -H "X-API-KEY: $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "mainetservice-accounts-get-payee-names",
    "desc": "POC Route 1: Spring MVC REST - Accounts Payee Names Lookup",
    "uri": "/MainetService/rest/AccountReceiptEntry/doGetPayeeNames",
    "methods": ["POST"],
    "upstream": {
      "type": "roundrobin",
      "nodes": {
        "host.docker.internal:8080": 1
      },
      "timeout": {
        "connect": 6,
        "send": 6,
        "read": 60
      },
      "pass_host": "pass"
    }
  }'

# Route 2: Apache CXF REST (License Dashboard Year-wise Count)
ROUTE2_ID="mainetservice-common-license-yearwise-count"
echo -n "Configuring Route 2 ($ROUTE2_ID)... "
curl -s -o /dev/null -w "%{http_code}\n" -X PUT "$ADMIN_URL/routes/$ROUTE2_ID" \
  -H "X-API-KEY: $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "mainetservice-common-license-yearwise-count",
    "desc": "POC Route 2: Apache CXF REST - Year-wise License Count Statistics",
    "uri": "/MainetService/services/rest/common/licenseDashboardGraph/getYearWiseLicenseCount",
    "methods": ["GET"],
    "upstream": {
      "type": "roundrobin",
      "nodes": {
        "host.docker.internal:8080": 1
      },
      "timeout": {
        "connect": 6,
        "send": 6,
        "read": 60
      },
      "pass_host": "pass"
    }
  }'

echo ""
echo "Active Routes List from Admin API:"
curl -s -X GET "$ADMIN_URL/routes" -H "X-API-KEY: $API_KEY" | jq . || cat
echo ""
echo "Routes provisioned successfully."
