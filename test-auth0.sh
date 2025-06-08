#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Get Auth0 token
echo "Getting Auth0 token..."
TOKEN_RESPONSE=$(curl -s -X POST \
  -H "Content-Type: application/json" \
  -d "{
    \"client_id\": \"$AUTH0_CLIENT_ID\",
    \"client_secret\": \"$AUTH0_CLIENT_SECRET\",
    \"audience\": \"$AUTH0_AUDIENCE\",
    \"grant_type\": \"client_credentials\"
  }" \
  "https://$AUTH0_DOMAIN/oauth/token")

# Extract token
TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.access_token')

if [ "$TOKEN" == "null" ]; then
    echo -e "${RED}Failed to get token. Response:${NC}"
    echo $TOKEN_RESPONSE
    exit 1
fi

echo -e "${GREEN}Successfully got token${NC}"

# Test public endpoint (should work without token)
echo -e "\nTesting public endpoint..."
PUBLIC_RESPONSE=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/v1/test/public)
HTTP_CODE=$(echo "$PUBLIC_RESPONSE" | tail -n1)
BODY=$(echo "$PUBLIC_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" == "200" ]; then
    echo -e "${GREEN}Public endpoint test passed${NC}"
    echo "Response: $BODY"
else
    echo -e "${RED}Public endpoint test failed${NC}"
    echo "Status: $HTTP_CODE"
    echo "Response: $BODY"
fi

# Test protected endpoint without token (should fail)
echo -e "\nTesting protected endpoint without token..."
PROTECTED_RESPONSE=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/v1/test/protected)
HTTP_CODE=$(echo "$PROTECTED_RESPONSE" | tail -n1)
BODY=$(echo "$PROTECTED_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" == "401" ]; then
    echo -e "${GREEN}Protected endpoint test without token passed (correctly rejected)${NC}"
else
    echo -e "${RED}Protected endpoint test without token failed${NC}"
    echo "Status: $HTTP_CODE"
    echo "Response: $BODY"
fi

# Test protected endpoint with token
echo -e "\nTesting protected endpoint with token..."
PROTECTED_RESPONSE=$(curl -s -w "\n%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/test/protected)
HTTP_CODE=$(echo "$PROTECTED_RESPONSE" | tail -n1)
BODY=$(echo "$PROTECTED_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" == "200" ]; then
    echo -e "${GREEN}Protected endpoint test with token passed${NC}"
    echo "Response: $BODY"
else
    echo -e "${RED}Protected endpoint test with token failed${NC}"
    echo "Status: $HTTP_CODE"
    echo "Response: $BODY"
fi 
