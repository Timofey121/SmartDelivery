#!/bin/bash

# SmartDelivery Microservices Stop Script
# This script stops all running microservices

echo "🛑 Stopping SmartDelivery Microservices..."

# Function to kill process by PID file
kill_service() {
    local service_name=$1
    local pid_file="logs/$service_name.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            echo "🛑 Stopping $service_name (PID: $pid)..."
            kill $pid
            sleep 2
            
            # Force kill if still running
            if ps -p $pid > /dev/null 2>&1; then
                echo "🔨 Force killing $service_name..."
                kill -9 $pid
            fi
            
            echo "✅ $service_name stopped"
        else
            echo "ℹ️  $service_name was not running"
        fi
        rm -f "$pid_file"
    else
        echo "ℹ️  No PID file found for $service_name"
    fi
}

# Stop all services
kill_service "eureka"
kill_service "gateway"
kill_service "auth"
kill_service "user"
kill_service "order"
kill_service "notification"

# Clean up any remaining Java processes related to SmartDelivery
echo "🧹 Cleaning up any remaining SmartDelivery processes..."
pkill -f "smartdelivery" 2>/dev/null || true
pkill -f "SmartDelivery" 2>/dev/null || true

echo "✅ All services stopped!"
echo "📝 Log files are preserved in the logs/ directory"
