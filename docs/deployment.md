# SmartDelivery Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Docker Deployment](#docker-deployment)
4. [Production Deployment](#production-deployment)
5. [Monitoring and Maintenance](#monitoring-and-maintenance)
6. [Troubleshooting](#troubleshooting)

## Prerequisites

### System Requirements
- **Java**: 17 or higher
- **Maven**: 3.8 or higher
- **Docker**: 20.10 or higher (optional)
- **Docker Compose**: 2.0 or higher (optional)
- **Memory**: Minimum 4GB RAM
- **Storage**: Minimum 10GB free space

### External Dependencies
- **PostgreSQL**: 13 or higher
- **RabbitMQ**: 3.8 or higher

## Local Development Setup

### 1. Clone Repository
```bash
git clone https://github.com/your-username/SmartDelivery.git
cd SmartDelivery
```

### 2. Start Infrastructure Services

#### Option A: Using Docker Compose (Recommended)
```bash
# Start only infrastructure services
docker-compose up -d postgres rabbitmq

# Or start all services
docker-compose up -d
```

#### Option B: Manual Setup
```bash
# Start PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_DB=SmartDelivery \
  -e POSTGRES_USER=timofey \
  -e POSTGRES_PASSWORD=password \
  -p 5433:5432 \
  postgres:13

# Start RabbitMQ
docker run -d --name rabbitmq \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

### 3. Build and Start Services

#### Quick Start (Using Scripts)
```bash
# Make scripts executable
chmod +x start-services.sh stop-services.sh

# Start all services
./start-services.sh

# Stop all services
./stop-services.sh
```

#### Manual Start
```bash
# Build project
mvn clean install -DskipTests

# Start services in order
cd eureka-service && mvn spring-boot:run &
cd api-gateway && mvn spring-boot:run &
cd auth-service && mvn spring-boot:run &
cd user-service && mvn spring-boot:run &
cd order-service && mvn spring-boot:run &
cd notification-service && mvn spring-boot:run &
```

### 4. Verify Installation
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## Docker Deployment

### 1. Build Docker Images
```bash
# Build all services
mvn clean package -DskipTests

# Build individual services
docker build -t smartdelivery/eureka-service ./eureka-service
docker build -t smartdelivery/api-gateway ./api-gateway
docker build -t smartdelivery/auth-service ./auth-service
docker build -t smartdelivery/user-service ./user-service
docker build -t smartdelivery/order-service ./order-service
docker build -t smartdelivery/notification-service ./notification-service
```

### 2. Start with Docker Compose
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### 3. Environment Configuration
Create `.env` file:
```env
# Database
DB_HOST=postgres
DB_PORT=5432
DB_NAME=SmartDelivery
DB_USERNAME=timofey
DB_PASSWORD=password

# JWT
JWT_SECRET=your-super-secret-jwt-key-here
JWT_LIFETIME=10m

# RabbitMQ
RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
```

## Production Deployment

### 1. Infrastructure Setup

#### Database Setup
```sql
-- Create database
CREATE DATABASE smartdelivery_prod;

-- Create user
CREATE USER smartdelivery_user WITH PASSWORD 'secure_password';

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE smartdelivery_prod TO smartdelivery_user;
```

#### RabbitMQ Setup
```bash
# Create virtual host
rabbitmqctl add_vhost smartdelivery_prod

# Create user
rabbitmqctl add_user smartdelivery_user secure_password

# Set permissions
rabbitmqctl set_permissions -p smartdelivery_prod smartdelivery_user ".*" ".*" ".*"
```

### 2. Application Configuration

#### Production Properties
```yaml
# application-prod.yml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:postgresql://prod-db:5432/smartdelivery_prod
    username: smartdelivery_user
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  rabbitmq:
    host: ${RABBITMQ_HOST}
    port: ${RABBITMQ_PORT}
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}

jwt:
  secret: ${JWT_SECRET}
  lifetime: 1h

logging:
  level:
    com.smartdelivery: INFO
    org.springframework.security: WARN
```

### 3. Docker Production Setup

#### Production Docker Compose
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: smartdelivery_prod
      POSTGRES_USER: smartdelivery_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - smartdelivery-network

  rabbitmq:
    image: rabbitmq:3-management
    environment:
      RABBITMQ_DEFAULT_USER: ${RABBITMQ_USERNAME}
      RABBITMQ_DEFAULT_PASS: ${RABBITMQ_PASSWORD}
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    networks:
      - smartdelivery-network

  # ... other services with production configs
```

### 4. Load Balancer Configuration

#### Nginx Configuration
```nginx
upstream smartdelivery {
    server localhost:8080;
    server localhost:8081;
    server localhost:8082;
}

server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://smartdelivery;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## Monitoring and Maintenance

### 1. Health Checks
```bash
# Check service health
curl http://localhost:8080/actuator/health

# Check individual services
curl http://localhost:8761/actuator/health
curl http://localhost:8762/actuator/health
```

### 2. Log Monitoring
```bash
# View logs
docker-compose logs -f service-name

# Log aggregation (ELK Stack)
# Configure logstash to collect logs from all services
```

### 3. Performance Monitoring
```bash
# JVM metrics
curl http://localhost:8080/actuator/metrics

# Custom metrics
curl http://localhost:8080/actuator/metrics/orders.created
```

### 4. Database Maintenance
```sql
-- Regular maintenance
VACUUM ANALYZE;

-- Check table sizes
SELECT schemaname,tablename,pg_size_pretty(size) as size
FROM (
    SELECT schemaname, tablename, pg_total_relation_size(schemaname||'.'||tablename) as size
    FROM pg_tables
    WHERE schemaname = 'public'
) t
ORDER BY size DESC;
```

## Troubleshooting

### Common Issues

#### 1. Service Discovery Issues
```bash
# Check Eureka registration
curl http://localhost:8761/eureka/apps

# Restart Eureka service
docker-compose restart eureka-server
```

#### 2. Database Connection Issues
```bash
# Check database connectivity
docker exec -it postgres psql -U timofey -d SmartDelivery -c "SELECT 1;"

# Check connection pool
curl http://localhost:8762/actuator/health/db
```

#### 3. RabbitMQ Issues
```bash
# Check RabbitMQ status
docker exec -it rabbitmq rabbitmqctl status

# Check queues
docker exec -it rabbitmq rabbitmqctl list_queues
```

#### 4. Memory Issues
```bash
# Check JVM memory usage
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Increase heap size
export JAVA_OPTS="-Xmx2g -Xms1g"
```

### Performance Tuning

#### 1. JVM Tuning
```bash
# Production JVM settings
export JAVA_OPTS="-server -Xmx4g -Xms2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

#### 2. Database Tuning
```sql
-- PostgreSQL configuration
shared_buffers = 256MB
effective_cache_size = 1GB
work_mem = 4MB
maintenance_work_mem = 64MB
```

#### 3. Connection Pool Tuning
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
```

### Backup and Recovery

#### 1. Database Backup
```bash
# Create backup
pg_dump -h localhost -U timofey SmartDelivery > backup.sql

# Restore backup
psql -h localhost -U timofey SmartDelivery < backup.sql
```

#### 2. Configuration Backup
```bash
# Backup configuration files
tar -czf config-backup.tar.gz */src/main/resources/
```

### Security Considerations

#### 1. SSL/TLS Configuration
```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
```

#### 2. Environment Variables
```bash
# Use environment variables for secrets
export JWT_SECRET="your-super-secret-key"
export DB_PASSWORD="secure-db-password"
export RABBITMQ_PASSWORD="secure-rabbitmq-password"
```

#### 3. Network Security
```bash
# Firewall rules
ufw allow 8080/tcp  # API Gateway
ufw allow 8761/tcp  # Eureka
ufw deny 5432/tcp   # Database (internal only)
ufw deny 5672/tcp   # RabbitMQ (internal only)
```
