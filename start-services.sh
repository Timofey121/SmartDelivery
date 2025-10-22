#!/bin/bash

# SmartDelivery Microservices Startup Script
# This script starts all microservices in the correct order

echo "🚀 Starting SmartDelivery Microservices..."

# Function to check if a port is available
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo "❌ Port $1 is already in use"
        return 1
    else
        echo "✅ Port $1 is available"
        return 0
    fi
}

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1

    echo "⏳ Waiting for $service_name to be ready on port $port..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:$port/actuator/health >/dev/null 2>&1; then
            echo "✅ $service_name is ready!"
            return 0
        fi
        
        echo "⏳ Attempt $attempt/$max_attempts - $service_name not ready yet..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "❌ $service_name failed to start within expected time"
    return 1
}

# Check if required ports are available
echo "🔍 Checking port availability..."
check_port 8761 || exit 1  # Eureka
check_port 8080 || exit 1  # API Gateway
check_port 8762 || exit 1  # Auth Service
check_port 8763 || exit 1  # User Service
check_port 8764 || exit 1  # Notification Service
check_port 8765 || exit 1  # Order Service

# Build the project
echo "🔨 Building the project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful!"

# Start services in order
echo "🚀 Starting services..."

# 1. Start Eureka Server
echo "📡 Starting Eureka Server..."
cd eureka-service
mvn spring-boot:run > ../logs/eureka.log 2>&1 &
EUREKA_PID=$!
cd ..

# Wait for Eureka to be ready
sleep 10

# 2. Start API Gateway
echo "🌐 Starting API Gateway..."
cd api-gateway
mvn spring-boot:run > ../logs/gateway.log 2>&1 &
GATEWAY_PID=$!
cd ..

# 3. Start Auth Service
echo "🔐 Starting Auth Service..."
cd auth-service
mvn spring-boot:run > ../logs/auth.log 2>&1 &
AUTH_PID=$!
cd ..

# 4. Start User Service
echo "👤 Starting User Service..."
cd user-service
mvn spring-boot:run > ../logs/user.log 2>&1 &
USER_PID=$!
cd ..

# 5. Start Order Service
echo "📦 Starting Order Service..."
cd order-service
mvn spring-boot:run > ../logs/order.log 2>&1 &
ORDER_PID=$!
cd ..

# 6. Start Notification Service
echo "📧 Starting Notification Service..."
cd notification-service
mvn spring-boot:run > ../logs/notification.log 2>&1 &
NOTIFICATION_PID=$!
cd ..

# Create logs directory if it doesn't exist
mkdir -p logs

# Save PIDs for cleanup
echo $EUREKA_PID > logs/eureka.pid
echo $GATEWAY_PID > logs/gateway.pid
echo $AUTH_PID > logs/auth.pid
echo $USER_PID > logs/user.pid
echo $ORDER_PID > logs/order.pid
echo $NOTIFICATION_PID > logs/notification.pid

echo "✅ All services started!"
echo ""
echo "📊 Service Status:"
echo "  - Eureka Server: http://localhost:8761"
echo "  - API Gateway: http://localhost:8080"
echo "  - Auth Service: http://localhost:8762"
echo "  - User Service: http://localhost:8763"
echo "  - Order Service: http://localhost:8765"
echo "  - Notification Service: http://localhost:8764"
echo ""
echo "📝 Logs are available in the logs/ directory"
echo "🛑 To stop all services, run: ./stop-services.sh"
echo ""
echo "🎉 SmartDelivery is ready to use!"
