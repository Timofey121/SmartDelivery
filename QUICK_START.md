# 🚀 SmartDelivery - Quick Start Guide

## Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose (recommended)

## 🏃‍♂️ Quick Start (5 minutes)

### Option 1: Using Docker Compose (Recommended)
```bash
# 1. Clone and navigate to project
git clone <your-repo-url>
cd SmartDelivery

# 2. Start all services with Docker
docker-compose up -d

# 3. Wait for services to start (30-60 seconds)
# 4. Test the API
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","role":"USER"}'
```

### Option 2: Manual Setup
```bash
# 1. Start infrastructure
docker run -d --name postgres -e POSTGRES_DB=SmartDelivery -e POSTGRES_USER=timofey -e POSTGRES_PASSWORD=password -p 5433:5432 postgres:13
docker run -d --name rabbitmq -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# 2. Build and start services
mvn clean install -DskipTests
./start-services.sh
```

## 🔗 Service URLs
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## 🧪 Test the API
```bash
# Register user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","role":"USER"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Create order (use JWT token from login)
curl -X POST http://localhost:8080/orders/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"product":"Laptop","quantity":1,"price":999.99,"customerName":"John Doe"}'
```

## 🛑 Stop Services
```bash
# Docker Compose
docker-compose down

# Manual
./stop-services.sh
```

## 📚 Full Documentation
- [README.md](README.md) - Complete project overview
- [docs/architecture.md](docs/architecture.md) - System architecture
- [docs/api-documentation.md](docs/api-documentation.md) - API reference
- [docs/deployment.md](docs/deployment.md) - Deployment guide

## 🆘 Troubleshooting
- **Port conflicts**: Check if ports 8080, 8761-8765 are available
- **Database connection**: Ensure PostgreSQL is running on port 5433
- **RabbitMQ issues**: Check RabbitMQ management UI at http://localhost:15672
- **Service discovery**: Verify Eureka dashboard at http://localhost:8761

## 🎯 Next Steps
1. Explore the API endpoints
2. Check service logs in `logs/` directory
3. Monitor services via Eureka dashboard
4. Customize configuration in `application.yml` files
