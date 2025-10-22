# SmartDelivery API Documentation

## Base URL
All API requests should be made to the API Gateway:
```
http://localhost:8080
```

## Authentication

The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## API Endpoints

### Authentication Service

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "username": "string",
  "password": "string",
  "role": "USER|ADMIN|COURIER"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### User Service

#### Get Current User Profile
```http
GET /users/me
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "role": "USER"
}
```

#### Get All Users (Admin Only)
```http
GET /users
Authorization: Bearer <admin-token>
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "role": "USER"
  },
  {
    "id": 2,
    "username": "jane_smith",
    "role": "COURIER"
  }
]
```

### Order Service

#### Create Order
```http
POST /orders/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "product": "string",
  "quantity": 1,
  "price": 99.99,
  "customerName": "string"
}
```

**Response:**
```json
{
  "id": 1,
  "product": "Laptop",
  "quantity": 1,
  "price": 999.99,
  "customerName": "John Doe"
}
```

#### Get Order by ID
```http
GET /orders/{id}
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": 1,
  "product": "Laptop",
  "quantity": 1,
  "price": 999.99,
  "customerName": "John Doe"
}
```

### Notification Service

#### Send Notification
```http
POST /notifications/send
Authorization: Bearer <token>
Content-Type: application/json

{
  "message": "string",
  "recipient": "string"
}
```

**Response:**
```json
"Notification sent to john_doe"
```

#### Get Notification History
```http
GET /notifications/history
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "message": "New order created: Laptop",
    "recipient": "john_doe",
    "timestamp": "2024-01-15T10:30:00"
  }
]
```

## Error Responses

### 400 Bad Request
```json
{
  "error": "Bad Request",
  "message": "Validation failed",
  "status": 400
}
```

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Invalid credentials",
  "status": 401
}
```

### 403 Forbidden
```json
{
  "error": "Forbidden",
  "message": "Access denied",
  "status": 403
}
```

### 404 Not Found
```json
{
  "error": "Not Found",
  "message": "Resource not found",
  "status": 404
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "status": 500
}
```

## Rate Limiting

The API Gateway implements rate limiting to prevent abuse:
- **Authenticated users**: 1000 requests per hour
- **Unauthenticated users**: 100 requests per hour

## CORS

Cross-Origin Resource Sharing is enabled for:
- `http://localhost:3000` (Development)
- `https://yourdomain.com` (Production)

## API Versioning

Current API version: `v1`

Version is included in the URL path:
```
http://localhost:8080/v1/auth/login
```

## Webhooks

### Order Created Webhook
When an order is created, a webhook is sent to the notification service:

**Payload:**
```json
{
  "event": "order.created",
  "data": {
    "orderId": 1,
    "product": "Laptop",
    "customerName": "John Doe",
    "timestamp": "2024-01-15T10:30:00"
  }
}
```

## SDK Examples

### JavaScript/Node.js
```javascript
const axios = require('axios');

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Login
const login = async (username, password) => {
  const response = await api.post('/auth/login', { username, password });
  return response.data.token;
};

// Create order
const createOrder = async (token, orderData) => {
  const response = await api.post('/orders/create', orderData, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return response.data;
};
```

### Python
```python
import requests

class SmartDeliveryAPI:
    def __init__(self, base_url='http://localhost:8080'):
        self.base_url = base_url
        self.token = None
    
    def login(self, username, password):
        response = requests.post(f'{self.base_url}/auth/login', json={
            'username': username,
            'password': password
        })
        self.token = response.json()['token']
        return self.token
    
    def create_order(self, order_data):
        headers = {'Authorization': f'Bearer {self.token}'}
        response = requests.post(f'{self.base_url}/orders/create', 
                               json=order_data, headers=headers)
        return response.json()
```

### cURL Examples

#### Register User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "role": "USER"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### Create Order
```bash
curl -X POST http://localhost:8080/orders/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "product": "Laptop",
    "quantity": 1,
    "price": 999.99,
    "customerName": "John Doe"
  }'
```

## Testing

### Postman Collection
Import the SmartDelivery API collection from:
```
docs/postman/SmartDelivery-API.postman_collection.json
```

### Test Data
Use the following test accounts:
- **Admin**: `admin` / `admin123`
- **User**: `user` / `user123`
- **Courier**: `courier` / `courier123`
