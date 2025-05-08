# 🍷 eCellar – Backend

**eCellar** is the backend service of the wine cellar management and social platform.  
Built with **Spring Boot 3** and **Java**, it exposes secure REST APIs for user, wine, cellar, and marketplace management.

---

## 🚀 Features

- 🧑‍💼 User registration & JWT login
- 🍷 Full wine management (CRUD)
- 🔍 Advanced Elasticsearch-powered wine search
- 🧾 Import wines via CSV/XLS from other platforms (Vivino, etc.)
- 🧊 Private and public wine cellars
- 💬 Purchase proposal system (offers, acceptance, payment-ready)
- 🏠 Shipping address management
- 📁 Profile & cellar updates
- 🔐 Role-based authorization with JWT

---

## 🧱 Tech Stack

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Security + JWT**
- **Spring Data JPA** (PostgreSQL)
- **Elasticsearch**
- **MapStruct** (DTO mapping)
- **Apache POI** (CSV/XLS import)
- **Flyway** (optional DB migrations)

---

## ⚙️ Getting Started

### 🔗 Prerequisites

- Java 17+
- Maven
- PostgreSQL database
- Elasticsearch server running (default: `http://localhost:9200`)

### 🧪 Installation

1. Clone the repository:

```bash
git clone https://github.com/AndreaFreddiGo/eCellar_backend
cd ecellar-backend
```

2. Configure environment variables in `env.properties`:

```properties
SERVER_PORT=3001
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=your_password
POSTGRES_URL=jdbc:postgresql://localhost:5432/eCellar_backend
JWT_SECRET=your_jwt_secret
ELASTICSEARCH_URL=http://localhost:9200
ELASTICSEARCH_CONNECTION_TIMEOUT=5s
ELASTICSEARCH_SOCKET_TIMEOUT=10s
```

3. Start PostgreSQL and Elasticsearch, then run:

```bash
./mvnw spring-boot:run
```

The backend will be available at [http://localhost:3001](http://localhost:3001)

---

## 📁 Project Structure

```
src/
├── controllers/       # REST controllers
├── entities/          # JPA entities
├── dto/               # Request and response payloads
├── repositories/      # JPA and Elasticsearch repositories
├── services/          # Business logic layer
├── mappers/           # MapStruct DTO mappers
├── security/          # JWT and user auth config
├── elasticsearch/     # WineDocument mapping
└── resources/         # application.properties, env.properties
```

---

## 🔐 Authentication

- JWT issued after successful login
- Token must be included in requests:  
  `Authorization: Bearer <token>`
- Role-based route protection

---

## 🔁 API Endpoints

| Resource  | Endpoint Example                                 |
| --------- | ------------------------------------------------ |
| Auth      | `POST /auth/login`                               |
| Wines     | `GET /wines`, `POST /wines`, `GET /wines/search` |
| Cellar    | `GET /cellar`, `POST /cellar/public`             |
| Users     | `GET /users/me`, `PUT /users/update`             |
| Proposals | `POST /proposals`, `PUT /proposals/{id}`         |
| Address   | `POST /addresses`                                |
| Import    | `POST /users/import-csv`                         |

---

## 🐞 Bug Reports

Please [open an issue](https://github.com/AndreaFreddiGo/eCellar_backend/issues) or contact the maintainer.

---

## 👤 Maintainer

- Andrea Freddi

---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

## 🛡️ Copyright

© 2025 Andrea Freddi. All rights reserved.

The source code, architecture, and API design of this application are protected by copyright law.  
Unauthorized use, reproduction, or distribution is strictly prohibited without written permission.
