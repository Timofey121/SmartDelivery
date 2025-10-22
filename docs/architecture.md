# SmartDelivery Architecture

## System Overview

SmartDelivery is a microservices-based delivery management system built with Spring Boot and Spring Cloud. The system follows modern microservices patterns with service discovery, API gateway, and asynchronous messaging.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                      │
│                    (Web, Mobile, API Clients)                  │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      │ HTTP/HTTPS
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                    API Gateway                                  │
│                   (Port: 8080)                                 │
│              • Request Routing                                  │
│              • Load Balancing                                   │
│              • Authentication                                   │
│              • Rate Limiting                                    │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      │ Service Discovery
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                 Eureka Server                                   │
│                  (Port: 8761)                                  │
│              • Service Registry                                 │
│              • Health Monitoring                                │
│              • Load Balancing                                   │
└─────────────────────┬───────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│Auth Service │ │User Service │ │Order Service│
│ (Port: 8762)│ │ (Port: 8763)│ │ (Port: 8765)│
│             │ │             │ │             │
│• JWT Auth   │ │• User Mgmt  │ │• Order CRUD │
│• User Reg   │ │• Profiles   │ │• Validation │
│• Login      │ │• Roles      │ │• Business   │
│             │ │             │ │  Logic      │
└─────────────┘ └─────────────┘ └─────┬───────┘
                                      │
                                      │ RabbitMQ
                                      │
                              ┌───────▼───────┐
                              │Notification   │
                              │Service        │
                              │(Port: 8764)   │
                              │               │
                              │• Email        │
                              │• SMS          │
                              │• Push         │
                              │• History      │
                              └───────────────┘
```

## Data Flow

### 1. User Registration/Login Flow
```
Client → API Gateway → Auth Service → Database
                ↓
        JWT Token Response
```

### 2. Order Creation Flow
```
Client → API Gateway → Order Service → Database
                ↓              ↓
        Order Response    RabbitMQ → Notification Service
```

### 3. Service Discovery Flow
```
All Services → Eureka Server (Registration)
API Gateway → Eureka Server (Service Lookup)
```

## Technology Stack

### Backend Services
- **Java 17**: Modern Java features and performance
- **Spring Boot 3.4**: Rapid application development
- **Spring Cloud**: Microservices infrastructure
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data persistence layer

### Infrastructure
- **Eureka**: Service discovery and registration
- **Spring Cloud Gateway**: API gateway and routing
- **RabbitMQ**: Asynchronous messaging
- **PostgreSQL**: Primary database
- **H2**: Development database

### Development Tools
- **Maven**: Build and dependency management
- **Lombok**: Code generation
- **MapStruct**: Object mapping
- **Docker**: Containerization

## Security Architecture

### Authentication Flow
1. User submits credentials to Auth Service
2. Auth Service validates credentials
3. JWT token is generated and returned
4. Client includes JWT in subsequent requests
5. API Gateway validates JWT for protected routes

### Authorization
- Role-based access control (RBAC)
- JWT claims contain user roles
- Service-level authorization checks
- API Gateway route-level security

## Scalability Considerations

### Horizontal Scaling
- Stateless services enable easy scaling
- Load balancing through Eureka
- Database connection pooling
- RabbitMQ clustering support

### Performance Optimization
- Connection pooling
- Caching strategies
- Asynchronous processing
- Database indexing

## Monitoring and Observability

### Health Checks
- Spring Boot Actuator endpoints
- Eureka health monitoring
- Custom health indicators

### Logging
- Structured logging with SLF4J
- Centralized log collection ready
- Request/response logging

### Metrics
- Application metrics via Actuator
- Custom business metrics
- Performance monitoring

## Deployment Architecture

### Development
- Local development with H2 database
- Docker Compose for infrastructure
- Hot reloading support

### Production
- Containerized services
- External PostgreSQL database
- RabbitMQ cluster
- Load balancer configuration

## API Design

### RESTful APIs
- Standard HTTP methods
- Consistent response formats
- Proper HTTP status codes
- API versioning support

### Error Handling
- Global exception handling
- Consistent error responses
- Proper logging of errors
- Client-friendly error messages

## Data Management

### Database Design
- Normalized database schema
- Proper indexing strategy
- Foreign key relationships
- Data validation at multiple levels

### Data Consistency
- ACID transactions within services
- Eventual consistency between services
- Compensating transactions
- Saga pattern implementation
