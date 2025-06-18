# 🌿 NaturaLog – Backend API

**NaturaLog** is a modular Spring Boot REST API that powers the NaturaLog platform.  
It enables intelligent species search, AI-enhanced species descriptions, and personalized collections of biodiversity data.

This repository contains the **backend only**. The Angular frontend is available separately [here](https://github.com/ThomasRestiau/NaturaLog_frontend).

---

## 🔧 Features

- 🔎 Search species via GBIF API
- 🧠 Generate contextual descriptions using OpenAI (GPT)
- 📄 Serve detailed taxonomic and geographic data
- 💾 Manage a personal species collection per user
- 💬 Naturalist chatbot assistant
- 🔐 JWT-based authentication (login/register)
- 🧩 Clean layered architecture (DTO, service, controller, entity)
- 📘 API documentation with Swagger (OpenAPI)

---

## 🛠 Tech Stack

| Component        | Technology            |
|------------------|------------------------|
| Language         | Java 17                |
| Framework        | Spring Boot            |
| Database         | PostgreSQL             |
| ORM              | Hibernate (JPA)        |
| Security         | Spring Security + JWT  |
| External APIs    | GBIF, OpenAI GPT       |
| Documentation    | OpenAPI (Swagger UI)   |
| DevOps           | Docker                 |

---

## ⚙️ Configuration

This backend expects environment variables to be provided by a `.env` file  
**located at the root of your project (next to your `docker-compose.yml`)**, outside this repository.

```env
# .env (root project level)
OPENAI_API_KEY=your-key-here

# Optional if not using application.yml defaults:
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/your-db
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
```

---

## 🚀 Running the Backend 

### ✅ Option 1 – Using Docker Compose (recommended).

From the root folder where your docker-compose.yml and .env are located:

```
#bash

docker compose up --build
```

API available at:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui.html

### ✅ Option 2 – Local Development (without Docker)
If you want to run the backend independently:

```
#bash

./mvnw spring-boot:run
```
Make sure to provide required environment variables via your IDE or terminal.

---

## 🔗 Related Repository
[NaturaLog_frontend](https://github.com/ThomasRestiau/NaturaLog_frontend)

---

## 🧠 About
NaturaLog is part of my personal portfolio as a developer in reconversion.
It reflects my passion for structured backend development, nature, and meaningful digital tools.

---

## 📫 Contact
GitHub: https://github.com/ThomasRestiau

LinkedIn: https://linkedin.com/in/thomas-restiau

Email: thomas.restiau@live.be