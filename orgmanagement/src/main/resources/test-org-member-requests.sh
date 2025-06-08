#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/v1"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Testing Organization Member Management API${NC}\n"

# First, create an organization to work with
echo -e "${GREEN}Creating test organization...${NC}"
CREATE_ORG_RESPONSE=$(curl -s -X POST "${BASE_URL}/orgs" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Organization",
    "description": "Organization for testing member operations"
  }')
echo "$CREATE_ORG_RESPONSE"
ORG_ID=$(echo $CREATE_ORG_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "\nCreated organization with ID: $ORG_ID\n"

# 1. Add a user to the organization
echo -e "${GREEN}1. Adding a user to the organization...${NC}"
curl -s -X POST "${BASE_URL}/orgmembers" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "USER",
    "orgId": "'$ORG_ID'",
    "entityId": "11111111-1111-1111-1111-111111111111"
  }' | json_pp
echo -e "\n"

# 2. Add a group to the organization
echo -e "${GREEN}2. Adding a group to the organization...${NC}"
curl -s -X POST "${BASE_URL}/orgmembers" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "GROUP",
    "orgId": "'$ORG_ID'",
    "entityId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
  }' | json_pp
echo -e "\n"

# 3. Add a role to the organization
echo -e "${GREEN}3. Adding a role to the organization...${NC}"
curl -s -X POST "${BASE_URL}/orgmembers" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "ROLE",
    "orgId": "'$ORG_ID'",
    "entityId": "dddddddd-dddd-dddd-dddd-dddddddddddd"
  }' | json_pp
echo -e "\n"

# 4. Get organization members
echo -e "${GREEN}4. Getting organization members...${NC}"
curl -s -X GET "${BASE_URL}/orgmembers/${ORG_ID}" | json_pp
echo -e "\n"

# 5. Remove a user from the organization
echo -e "${GREEN}5. Removing a user from the organization...${NC}"
curl -s -X DELETE "${BASE_URL}/orgmembers/${ORG_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "USER",
    "orgId": "'$ORG_ID'",
    "entityId": "11111111-1111-1111-1111-111111111111"
  }'
echo -e "\n"

# 6. Verify user removal
echo -e "${GREEN}6. Verifying user removal...${NC}"
curl -s -X GET "${BASE_URL}/orgmembers/${ORG_ID}" | json_pp
echo -e "\n"

# 7. Add another user to the organization
echo -e "${GREEN}7. Adding another user to the organization...${NC}"
curl -s -X POST "${BASE_URL}/orgmembers" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "USER",
    "orgId": "'$ORG_ID'",
    "entityId": "22222222-2222-2222-2222-222222222222"
  }' | json_pp
echo -e "\n"

# 8. Get organization members again
echo -e "${GREEN}8. Getting organization members again...${NC}"
curl -s -X GET "${BASE_URL}/orgmembers/${ORG_ID}" | json_pp
echo -e "\n"

# 9. Remove the organization (this will cascade delete all members)
echo -e "${GREEN}9. Removing the organization...${NC}"
curl -s -X DELETE "${BASE_URL}/orgs/${ORG_ID}"
echo -e "\n"

# 10. Verify organization removal
echo -e "${GREEN}10. Verifying organization removal...${NC}"
curl -s -X GET "${BASE_URL}/orgs/${ORG_ID}" | json_pp
echo -e "\n" 