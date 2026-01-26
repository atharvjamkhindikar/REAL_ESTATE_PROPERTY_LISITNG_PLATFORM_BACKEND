# Real Estate Backend API

Spring Boot backend for Real Estate Listing Application with H2 database and proper ER relationships.

## Features
- CRUD operations for property listings
- User management (Agents, Owners, Buyers, Admins)
- Multiple images per property
- Property filtering by city, type, listing type, and price range
- H2 in-memory database with auto-initialization
- RESTful API endpoints
- CORS enabled for React frontend
- Proper Entity-Relationship mapping

## Database Structure

### Entity Relationships
- **User ↔ Property**: One-to-Many (One user can own/list multiple properties)
- **Property ↔ PropertyImage**: One-to-Many (One property can have multiple images)

### Tables
1. **USERS** - Stores agents, owners, buyers, and admins
2. **PROPERTIES** - Stores property listings
3. **PROPERTY_IMAGES** - Stores multiple images per property

📊 See [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for detailed ER diagram and table structures.

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Properties
- `GET /api/properties` - Get all properties
- `GET /api/properties/available` - Get available properties
- `GET /api/properties/{id}` - Get property by ID
- `POST /api/properties` - Create new property
- `PUT /api/properties/{id}` - Update property
- `DELETE /api/properties/{id}` - Delete property
- `GET /api/properties/city/{city}` - Filter by city
- `GET /api/properties/type/{propertyType}` - Filter by property type
- `GET /api/properties/listing-type/{listingType}` - Filter by listing type
- `GET /api/properties/price-range?minPrice=X&maxPrice=Y` - Filter by price range

## H2 Console
Access H2 database console at: `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:realestatedb`
- Username: `sa`
- Password: (empty)

## Database Configuration

The application uses enhanced H2 configuration with:
- Auto-initialization with sample data
- SQL logging enabled for debugging
- Proper indexing for performance
- Entity relationships with cascade operations

See `application.properties` for full configuration.

## Sample Data

The application automatically loads sample data on startup:
- 3 Users (2 Agents, 1 Owner)
- 5 Properties (various types)
- Multiple images per property

## Property Types
- HOUSE
- APARTMENT
- CONDO
- TOWNHOUSE
- LAND
- COMMERCIAL

## Listing Types
- FOR_SALE
- FOR_RENT

## User Types
- AGENT
- OWNER
- BUYER
- ADMIN

## Technologies
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database
- Hibernate
- Lombok
- Bean Validation
- Maven
