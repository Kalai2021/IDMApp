# User Management Service with Auth0 Integration

This project implements a user management service with Auth0 authentication and authorization.

## Prerequisites

- Java 21
- Maven
- PostgreSQL
- Auth0 Account
- Postman (for API testing)

## Setup

### 1. Database Setup

1. Create a PostgreSQL database:
```bash
createdb yourdbname
```

2. Update database credentials in `idmapp/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/yourdbname
    username: yourusername
    password: yourpassword
```

3. The application will automatically create the required tables using the schema.sql file.

### 2. Auth0 Setup

1. Log in to your [Auth0 Dashboard](https://manage.auth0.com/)
2. Create a new API:
   - Go to "Applications" > "APIs"
   - Click "Create API"
   - Name: "User Management API"
   - Identifier (audience): `/api/v1/`
   - Signing Algorithm: RS256

3. Create a new Machine-to-Machine Application:
   - Go to "Applications" > "Applications"
   - Click "Create Application"
   - Name: "User Management Client"
   - Type: "Machine to Machine"
   - Select your API from step 2
   - Grant all permissions

4. Note down the following values:
   - Domain (e.g., `your-tenant.auth0.com`)
   - Client ID
   - Client Secret
   - Audience (API Identifier)

### 3. Environment Configuration

1. Set environment variables:
```bash
export AUTH0_DOMAIN=your-tenant.auth0.com
export AUTH0_AUDIENCE=/api/v1/
export AUTH0_CLIENT_ID=your-client-id
export AUTH0_CLIENT_SECRET=your-client-secret
```

2. Update `idmapp/src/main/resources/application.yml` with your database credentials.

### 4. Build and Run

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
cd idmapp
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing with Postman

1. Import the Postman collection and environment:
   - Open Postman
   - Click "Import"
   - Select `auth0-collection.json` and `auth0-environment.json`

2. Configure the environment:
   - Click on the environment dropdown
   - Select "Auth0 Environment"
   - Click "Edit"
   - Update the following variables:
     - `auth0_domain`: Your Auth0 domain
     - `auth0_client_id`: Your application client ID
     - `auth0_client_secret`: Your application client secret
     - `auth0_audience`: `/api/v1/`
     - `base_url`: `http://localhost:8080`

3. Test the API:
   - Run "Get Auth0 Token" first to get an access token
   - The token will be automatically saved to the environment
   - Use the User Management requests to test CRUD operations

## API Endpoints

### User Management

- `POST /api/v1/users` - Create a new user
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

All endpoints require a valid Auth0 JWT token in the Authorization header:
```
Authorization: Bearer <token>
```

## Security

The application uses Auth0 for authentication and authorization:
- JWT tokens are validated using Auth0's JWKS endpoint
- Permissions are extracted from the token
- All `/api/v1/**` endpoints are protected
- CORS is enabled for development

## Development

### Project Structure

```
.
├── idmapp/                          # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/idmapp/
│   │   │   │       └── IdmappApplication.java
│   │   │   └── resources/
│   │   │       └── application.yml  # Main configuration
│   │   └── test/
│   └── pom.xml
├── usermanagement/                  # User management module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/usermanagement/
│   │   │   │       ├── config/      # Security and other configurations
│   │   │   │       ├── controller/  # REST controllers
│   │   │   │       ├── model/       # Domain models
│   │   │   │       ├── repository/  # Data access
│   │   │   │       └── service/     # Business logic
│   │   │   └── resources/
│   │   │       └── schema.sql       # Database schema
│   │   └── test/
│   └── pom.xml
└── pom.xml                          # Parent POM
```

### Adding New Features

1. Create new models in the appropriate module's `model` package
2. Add repositories in the module's `repository` package
3. Implement business logic in the module's `service` package
4. Create controllers in the module's `controller` package
5. Update security configuration if needed

## Troubleshooting

1. Token Issues:
   - Check if the token is expired
   - Verify the audience matches your API identifier
   - Ensure the token has the required permissions

2. Database Issues:
   - Check PostgreSQL is running
   - Verify database credentials
   - Check schema.sql is being executed

3. Application Issues:
   - Check application logs
   - Verify environment variables
   - Ensure all dependencies are resolved

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Environment Setup

### Auth0 Configuration

1. Create a new Auth0 application:
   - Go to [Auth0 Dashboard](https://manage.auth0.com/)
   - Navigate to Applications > Applications
   - Click "Create Application"
   - Name it "User Management Service"
   - Select "Machine to Machine Application" as the type
   - Click "Create"

2. Configure the application:
   - In the application settings, note down:
     - Domain (e.g., `your-tenant.auth0.com`)
     - Client ID
     - Client Secret
   - Under "APIs", create a new API:
     - Name: "User Management API"
     - Identifier: `/api/v1/`
     - Signing Algorithm: RS256

3. Set up Postman environment:
   - Import `auth0-environment.json` into Postman
   - Update the following values:
     - `auth0_domain`: Your Auth0 domain
     - `auth0_client_id`: Your application's client ID
     - `auth0_client_secret`: Your application's client secret
     - `auth0_audience`: `/api/v1/`
     - `base_url`: Your service URL (default: `http://localhost:8080`)

4. Run the tests:
   - Import the Postman collections from `postman_collections/`
   - Select the configured environment
   - Run the tests using the collection runner

### Local Development

1. Start the service:
```bash
./mvnw spring-boot:run
```

2. Run the tests:
```bash
./test-auth0.sh
```

## Security

- Never commit sensitive information like client secrets to version control
- Use environment variables or secure secret management for production
- Keep your Auth0 credentials secure and rotate them regularly

## API Documentation

The API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

# IDMApp

## Environment Setup

1. Copy the template configuration file:
```bash
cp idmapp/src/main/resources/application.yml.template idmapp/src/main/resources/application.yml
```

2. Set up the following environment variables:

### Database Configuration
- `DB_NAME`: Your PostgreSQL database name
- `DB_USERNAME`: Your PostgreSQL username
- `DB_PASSWORD`: Your PostgreSQL password

### Auth0 Configuration
- `AUTH0_DOMAIN`: Your Auth0 domain (e.g., dev-xxxxx.us.auth0.com)
- `AUTH0_AUDIENCE`: Your API audience (e.g., /api/v1/)
- `AUTH0_CLIENT_ID`: Your Auth0 application client ID
- `AUTH0_CLIENT_SECRET`: Your Auth0 application client secret

### OpenFGA Configuration
- `OPENFGA_API_URL`: Your OpenFGA API URL
- `OPENFGA_STORE_ID`: Your OpenFGA store ID
- `OPENFGA_API_TOKEN`: Your OpenFGA API token

You can set these environment variables in your shell or create a `.env` file in the project root.

## Security Notice

Never commit the `application.yml` file containing actual credentials to version control. The template file (`application.yml.template`) is provided as a reference for required configuration.

## Running the Application

1. Ensure PostgreSQL is running and the database is created
2. Set up all required environment variables
3. Run the application:
```bash
./mvnw spring-boot:run -pl idmapp
```
