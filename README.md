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
- **Delivery Service** — назначение курьеров и обновление статуса доставки
- **Notification Service** — отправка уведомлений через RabbitMQ

## 🔧 Технологии

- Java 17, Spring Boot 3.4
- Spring Cloud Netflix Eureka, Spring Cloud Gateway
- Spring Security + JWT (jjwt)
- Spring Data JPA, H2 (dev) / PostgreSQL (prod)
- RabbitMQ
- Docker, Docker Compose
- Lombok, Hibernate Validator

## 📦 Модули проекта

| Модуль               | Порт | Описание                                |
|----------------------|------|-----------------------------------------|
| eureka-server        | 8761 | Сервис регистрации и обнаружения        |
| api-gateway          | 8080 | API Gateway                             |
| auth-service         | 8762 | Регистрация, логин, JWT                 |
| user-service         | 8763 | Профили пользователей, курьеров         |
| order-service        | 8765 | Создание и получение заказов            |
| delivery-service     | 8766 | Назначение курьеров, статус доставки    |
| notification-service | 8767 | Отправка уведомлений (RabbitMQ consumer)|

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
cd delivery-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### 4. Запуск через Docker Compose
```bash
docker-compose up -d
```
> В `docker-compose.yml` описаны: Eureka, Gateway, все сервисы, RabbitMQ, PostgreSQL.

## ⚙️ Конфигурация
Конфигурационные файлы (`application.yml`) каждого сервиса находятся в соответствующих модулях. Основные параметры:
- `eureka.client.service-url.defaultZone`
- `spring.application.name`
- Настройки JWT (`jwt.secret`, `jwt.expiration`)
- Настройки RabbitMQ (`spring.rabbitmq.*`)
- Порты сервисов

## 🎯 Endpoints (пример)

### Auth Service
- `POST /auth/register` — регистрация
- `POST /auth/login` — получение JWT

### Order Service
- `POST /orders` — создать заказ
- `GET /orders/{id}` — получить заказ по ID


## 🛠️ Тестирование
- В проекте настроены unit и integration тесты (JUnit 5, Mockito).
- Запустить все тесты:
```bash
mvn test
```

## 🚚 Docker
- Каждый модуль имеет `Dockerfile`.
- При сборке:
```bash
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=timofey/smartdelivery-<module>
```

## 👨‍💻 Вклад
1. Форкай репозиторий
2. Создавай ветку `feature/...` или `bugfix/...`
3. Пушь и открывай PR

## 📜 Лицензия
MIT © Timofey

