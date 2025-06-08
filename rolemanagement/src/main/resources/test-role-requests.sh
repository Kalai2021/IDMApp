#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api/v1"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}Testing Role Management API${NC}\n"

# 1. Create a new role
echo -e "${GREEN}1. Creating a new role...${NC}"
CREATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/roles" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "qa_engineer",
    "displayName": "QA Engineer",
    "description": "Quality Assurance Engineer role with testing responsibilities"
  }')
echo "$CREATE_RESPONSE"
ROLE_ID=$(echo $CREATE_RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "\nCreated role with ID: $ROLE_ID\n"

# 2. Get all roles
echo -e "${GREEN}2. Getting all roles...${NC}"
curl -s -X GET "${BASE_URL}/roles" | json_pp
echo -e "\n"

# 3. Get role by ID
echo -e "${GREEN}3. Getting role by ID...${NC}"
curl -s -X GET "${BASE_URL}/roles/${ROLE_ID}" | json_pp
echo -e "\n"

# 4. Update role
echo -e "${GREEN}4. Updating role...${NC}"
curl -s -X PUT "${BASE_URL}/roles/${ROLE_ID}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "senior_qa_engineer",
    "displayName": "Senior QA Engineer",
    "description": "Senior Quality Assurance Engineer role with testing and team lead responsibilities"
  }' | json_pp
echo -e "\n"

# 5. Get updated role
echo -e "${GREEN}5. Getting updated role...${NC}"
curl -s -X GET "${BASE_URL}/roles/${ROLE_ID}" | json_pp
echo -e "\n"

# 6. Create another role for testing
echo -e "${GREEN}6. Creating another role...${NC}"
CREATE_RESPONSE2=$(curl -s -X POST "${BASE_URL}/roles" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "dev_ops",
    "displayName": "DevOps Engineer",
    "description": "DevOps Engineer role with CI/CD and infrastructure responsibilities"
  }')
echo "$CREATE_RESPONSE2"
ROLE_ID2=$(echo $CREATE_RESPONSE2 | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo -e "\nCreated role with ID: $ROLE_ID2\n"

# 7. Get all roles again to verify both exist
echo -e "${GREEN}7. Getting all roles again...${NC}"
curl -s -X GET "${BASE_URL}/roles" | json_pp
echo -e "\n"

# 8. Delete the first role
echo -e "${GREEN}8. Deleting the first role...${NC}"
curl -s -X DELETE "${BASE_URL}/roles/${ROLE_ID}"
echo -e "\n"

# 9. Verify deletion by trying to get the deleted role
echo -e "${GREEN}9. Verifying deletion...${NC}"
curl -s -X GET "${BASE_URL}/roles/${ROLE_ID}" | json_pp
echo -e "\n"

# 10. Get all roles one final time
echo -e "${GREEN}10. Getting all roles one final time...${NC}"
curl -s -X GET "${BASE_URL}/roles" | json_pp
echo -e "\n" 