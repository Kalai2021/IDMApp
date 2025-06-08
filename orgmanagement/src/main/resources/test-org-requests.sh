#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/v1"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Testing Organization Management API${NC}\n"

# 1. Create a new organization
echo -e "${GREEN}1. Creating a new organization...${NC}"
CREATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/orgs" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Engineering",
    "description": "Engineering department responsible for product development"
  }')
echo "$CREATE_RESPONSE"
ORG_ID=$(echo $CREATE_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "\nCreated organization with ID: $ORG_ID\n"

# 2. Get all organizations
echo -e "${GREEN}2. Getting all organizations...${NC}"
curl -s -X GET "${BASE_URL}/orgs" | json_pp
echo -e "\n"

# 3. Get organization by ID
echo -e "${GREEN}3. Getting organization by ID...${NC}"
curl -s -X GET "${BASE_URL}/orgs/${ORG_ID}" | json_pp
echo -e "\n"

# 4. Update organization
echo -e "${GREEN}4. Updating organization...${NC}"
curl -s -X PUT "${BASE_URL}/orgs/${ORG_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Product Engineering",
    "description": "Product Engineering department responsible for product development and innovation"
  }' | json_pp
echo -e "\n"

# 5. Get updated organization
echo -e "${GREEN}5. Getting updated organization...${NC}"
curl -s -X GET "${BASE_URL}/orgs/${ORG_ID}" | json_pp
echo -e "\n"

# 6. Create another organization
echo -e "${GREEN}6. Creating another organization...${NC}"
CREATE_RESPONSE2=$(curl -s -X POST "${BASE_URL}/orgs" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sales",
    "description": "Sales department responsible for revenue generation"
  }')
echo "$CREATE_RESPONSE2"
ORG_ID2=$(echo $CREATE_RESPONSE2 | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "\nCreated organization with ID: $ORG_ID2\n"

# 7. Get all organizations again
echo -e "${GREEN}7. Getting all organizations again...${NC}"
curl -s -X GET "${BASE_URL}/orgs" | json_pp
echo -e "\n"

# 8. Delete the first organization
echo -e "${GREEN}8. Deleting the first organization...${NC}"
curl -s -X DELETE "${BASE_URL}/orgs/${ORG_ID}"
echo -e "\n"

# 9. Verify deletion
echo -e "${GREEN}9. Verifying deletion...${NC}"
curl -s -X GET "${BASE_URL}/orgs/${ORG_ID}" | json_pp
echo -e "\n"

# 10. Get all organizations one final time
echo -e "${GREEN}10. Getting all organizations one final time...${NC}"
curl -s -X GET "${BASE_URL}/orgs" | json_pp
echo -e "\n" 