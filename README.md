# SmartDelivery 🚚

A comprehensive microservices-based delivery management system built with Spring Boot, featuring intelligent order processing, real-time notifications, and scalable architecture.

## 📖 Overview

SmartDelivery is a modern microservices architecture that provides user registration, order management, delivery tracking, and asynchronous notifications through RabbitMQ. The system uses API Gateway for centralized routing and Eureka for service discovery.

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │────│  Eureka Server  │────│  Auth Service   │
│   (Port: 8080)  │    │  (Port: 8761)   │    │  (Port: 8762)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  User Service   │    │ Order Service   │    │Notification Svc │
│  (Port: 8763)   │    │ (Port: 8765)    │    │ (Port: 8764)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                └─────── RabbitMQ ──────┘
```

## 🚀 Services

| Service | Port | Description |
|---------|------|-------------|
| **Eureka Server** | 8761 | Service registry and discovery |
| **API Gateway** | 8080 | Centralized entry point with routing |
| **Auth Service** | 8762 | Authentication & authorization (JWT) |
| **User Service** | 8763 | User profiles and courier management |
| **Order Service** | 8765 | Order creation and management |
| **Notification Service** | 8764 | Real-time notifications via RabbitMQ |

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.4
- **Service Discovery**: Spring Cloud Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Security**: Spring Security + JWT (jjwt)
- **Database**: Spring Data JPA, H2 (dev) / PostgreSQL (prod)
- **Message Queue**: RabbitMQ
- **Build Tool**: Maven
- **Additional**: Lombok, Hibernate Validator

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker & Docker Compose (optional)
- RabbitMQ (if not using Docker)
- PostgreSQL (for production)

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/SmartDelivery.git
cd SmartDelivery
```

### 2. Build the Project

```bash
mvn clean install -DskipTests
```

### 3. Start Infrastructure Services

#### Option A: Using Docker Compose (Recommended)

```bash
# Start RabbitMQ and PostgreSQL
docker-compose up -d
```

#### Option B: Manual Setup

1. **Start RabbitMQ**:
   ```bash
   # Using Docker
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   
   # Or install locally and start the service
   ```

2. **Start PostgreSQL**:
   ```bash
   # Using Docker
   docker run -d --name postgres -e POSTGRES_DB=SmartDelivery -e POSTGRES_USER=timofey -e POSTGRES_PASSWORD=password -p 5433:5432 postgres:13
   ```

### 4. Start Microservices

Start services in the following order:

```bash
# 1. Start Eureka Server
cd eureka-service
mvn spring-boot:run

# 2. Start API Gateway (in a new terminal)
cd api-gateway
mvn spring-boot:run

# 3. Start Auth Service (in a new terminal)
cd auth-service
mvn spring-boot:run

# 4. Start User Service (in a new terminal)
cd user-service
mvn spring-boot:run

# 5. Start Order Service (in a new terminal)
cd order-service
mvn spring-boot:run

# 6. Start Notification Service (in a new terminal)
cd notification-service
mvn spring-boot:run
```

### 5. Verify Installation

1. **Eureka Dashboard**: http://localhost:8761
2. **API Gateway**: http://localhost:8080
3. **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## 📋 API Endpoints

### Authentication Service
- `POST /auth/register` - User registration
- `POST /auth/login` - User login (returns JWT token)

### User Service
- `GET /users/me` - Get current user profile
- `GET /users` - Get all users (admin only)

### Order Service
- `POST /orders/create` - Create new order
- `GET /orders/{id}` - Get order by ID

### Notification Service
- `POST /notifications/send` - Send notification
- `GET /notifications/history` - Get notification history

## ⚙️ Configuration

### Environment Variables

Create `.env` file in the root directory:

```env
# Database
DB_HOST=localhost
DB_PORT=5433
DB_NAME=SmartDelivery
DB_USERNAME=timofey
DB_PASSWORD=password

# JWT
JWT_SECRET=your-super-secret-jwt-key-here
JWT_LIFETIME=10m

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
```

### Service Configuration

Each service has its own `application.yml` with specific configurations:
- Database connections
- JWT settings
- RabbitMQ configuration
- Eureka client settings

## 🧪 Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run tests for specific service
cd auth-service && mvn test
```

### API Testing

Use the following curl commands to test the API:

```bash
# Register a new user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","role":"USER"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Create an order (use JWT token from login response)
curl -X POST http://localhost:8080/orders/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"product":"Laptop","quantity":1,"price":999.99,"customerName":"John Doe"}'
```

## 🐳 Docker Support

### Build Docker Images

```bash
# Build all services
mvn clean package -DskipTests
docker build -t smartdelivery/eureka-service ./eureka-service
docker build -t smartdelivery/api-gateway ./api-gateway
docker build -t smartdelivery/auth-service ./auth-service
docker build -t smartdelivery/user-service ./user-service
docker build -t smartdelivery/order-service ./order-service
docker build -t smartdelivery/notification-service ./notification-service
```

### Docker Compose

```bash
# Start all services with Docker Compose
docker-compose up -d
```

## 📊 Monitoring

- **Eureka Dashboard**: Service registry and health status
- **RabbitMQ Management**: Message queue monitoring
- **Application Logs**: Each service logs to console with structured logging

## 🔧 Development

### Project Structure

```
SmartDelivery/
├── eureka-service/          # Service discovery
├── api-gateway/            # API Gateway with routing
├── auth-service/           # Authentication & authorization
├── user-service/           # User management
├── order-service/          # Order processing
├── notification-service/   # Notification handling
├── pom.xml                # Parent POM
└── README.md              # This file
```

### Adding New Services

1. Create new module in parent POM
2. Add service to API Gateway routes
3. Configure Eureka client
4. Update this README

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Timofey** - [GitHub](https://github.com/Timofey121) - ale3jurtaev@gmail.com

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- Spring Cloud for microservices tools
- RabbitMQ for reliable messaging
- The open-source community

---

⭐ **Star this repository if you found it helpful!**