# SmartDelivery
Интеллектуальная микросервисная система управления доставкой

## 📖 Описание
SmartDelivery — набор Spring Boot микросервисов, обеспечивающих регистрацию пользователей, управление заказами и доставкой, асинхронные уведомления через RabbitMQ, маршрутизацию через API Gateway и сервис-обнаружение через Eureka.

## 🚀 Архитектура

![Архитектура](docs/architecture.png)

- **Eureka Server** — сервис регистрации и обнаружения
- **API Gateway** — централизованная точка входа (Spring Cloud Gateway)
- **Auth Service** — аутентификация/авторизация (JWT)
- **User Service** — управление пользователями и курьерами
- **Order Service** — создание и просмотр заказов
- **Notification Service** — отправка уведомлений через RabbitMQ

## 🔧 Технологии

- Java 17, Spring Boot 3.4
- Spring Cloud Netflix Eureka, Spring Cloud Gateway
- Spring Security + JWT (jjwt)
- Spring Data JPA, H2 (dev) / PostgreSQL (prod)
- RabbitMQ
- Lombok, Hibernate Validator

## 📦 Модули проекта

| Модуль               | Порт | Описание                                |
|----------------------|------|-----------------------------------------|
| eureka-server        | 8761 | Сервис регистрации и обнаружения        |
| api-gateway          | 8080 | API Gateway                             |
| auth-service         | 8762 | Регистрация, логин, JWT                 |
| user-service         | 8763 | Профили пользователей, курьеров         |
| order-service        | 8765 | Создание и получение заказов            |
| notification-service | 8764 | Отправка уведомлений (RabbitMQ consumer)|

## 🚀 Быстрый старт

### 1. Предварительные требования
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- RabbitMQ (если без Docker)

### 2. Сборка проекта
```bash
mvn clean install -DskipTests
```

### 3. Запуск локально
- Запустить **Eureka**: `cd eureka-server && mvn spring-boot:run`
- Запустить **Gateway**: `cd api-gateway && mvn spring-boot:run`
- Запустить остальные модули:
```bash
cd auth-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

## ⚙️ Конфигурация
Конфигурационные файлы (`application.yml`) каждого сервиса находятся в соответствующих модулях. Основные параметры:
- `eureka.client.service-url.defaultZone`
- `spring.application.name`
- Настройки JWT (`jwt.secret`, `jwt.expiration`)
- Настройки RabbitMQ (`spring.rabbitmq.*`)
- Настройки Postgresql (`spring.datasource.*`)
- Порты сервисов

## 🎯 Endpoints (пример)

### Auth Service
- `POST /auth/register` — регистрация
- `POST /auth/login` — получение JWT

### Order Service
- `POST /orders` — создать заказ
- `GET /orders/{id}` — получить заказ по ID


## 👨‍💻 Вклад
1. Форкай репозиторий
2. Создавай ветку `feature/...` или `bugfix/...`
3. Пушь и открывай PR


> **Контакты**  
> Timofey121 — ale3jurtaev@gmail.com  
