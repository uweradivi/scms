# SCMS — Smart Client & Contract Management System

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React_18-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Redux](https://img.shields.io/badge/Redux_Toolkit-593D88?style=for-the-badge&logo=redux&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

A full-stack enterprise web application for managing clients, contracts, documents, and real-time communications. Built with a Spring Boot REST API backend and a React + Vite single-page frontend.

---

## Features

### Authentication & Authorization
- JWT-based login and session management
- Role-based access control with four roles: **Admin**, **Manager**, **Staff**, and **Client**
- Each role sees a tailored dashboard and has scoped permissions

### Contract Lifecycle Management
Contracts move through a fully defined workflow:

```
DRAFT → SUBMITTED → APPROVED → ACTIVE → EXPIRING → EXPIRED
```

- Status transitions enforced server-side with business logic validation
- Visual lifecycle timeline on the contract detail page
- Automated expiry detection and status updates

### Real-Time Notifications
- WebSocket integration for live contract status updates
- Instant in-app notifications pushed to connected users on key events (approval, expiry warnings, new messages)

### Document Management
- Secure file uploads integrated with **Cloudinary**
- Documents attached to contracts with version tracking
- Role-restricted access to sensitive files

### AI Analytics Module
- Predictive analytics on contract renewal likelihood
- Automated risk scoring and flagging for at-risk contracts
- Insights dashboard visible to Admin and Manager roles

### Audit Logging
- Every user action is logged with timestamp, actor, and affected entity
- Full audit trail viewable by Admins for compliance and accountability

### API Documentation
- Full **OpenAPI/Swagger** docs available at `/swagger-ui.html`
- All endpoints documented with request/response schemas and auth requirements

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend Language | Java 17 |
| Backend Framework | Spring Boot 3.2 |
| API Style | RESTful (Spring MVC) |
| Auth | JWT (JSON Web Tokens) |
| ORM | Hibernate / JPA |
| Database | PostgreSQL 14 |
| Real-Time | WebSocket (STOMP) |
| File Storage | Cloudinary |
| Build Tool | Maven |
| API Docs | OpenAPI / Swagger |
| Frontend Framework | React 18 |
| Build Tool | Vite |
| State Management | Redux Toolkit |
| UI Library | Material UI (MUI) |
| HTTP Client | Axios |
| Containerization | Docker |

---

## Project Structure

```
scms/
├── backend/                  # Spring Boot REST API
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/scms/
│   │       │       ├── auth/           # JWT auth & security config
│   │       │       ├── contract/       # Contract lifecycle logic
│   │       │       ├── client/         # Client management
│   │       │       ├── document/       # Cloudinary file handling
│   │       │       ├── notification/   # WebSocket messaging
│   │       │       ├── audit/          # Audit logging
│   │       │       └── analytics/      # AI analytics module
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
└── frontend/                 # React + Vite SPA
    ├── src/
    │   ├── features/         # Redux slices per feature
    │   ├── pages/            # Route-level page components
    │   ├── components/       # Shared UI components
    │   ├── services/         # Axios API service layer
    │   └── App.jsx
    ├── index.html
    └── vite.config.js
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+
- A [Cloudinary](https://cloudinary.com/) account (free tier works)

### 1. Clone the Repository

```bash
git clone https://github.com/uweradivi/scms.git
cd scms
```

### 2. Backend Setup

```bash
cd backend

# Create the database
createdb scms_db

# Copy environment template and fill in your values
cp .env.example .env

# Run the application
./mvnw spring-boot:run
```

Backend runs at: `http://localhost:8081`  
Swagger UI: `http://localhost:8081/swagger-ui.html`

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Copy environment template
cp .env.example .env

# Start development server
npm run dev
```

Frontend runs at: `http://localhost:5173`

---

## Environment Variables

### Backend (`backend/.env`)

```env
DB_URL=jdbc:postgresql://localhost:5432/scms_db
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret_key
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### Frontend (`frontend/.env`)

```env
VITE_API_BASE_URL=http://localhost:8081/api
VITE_WS_URL=ws://localhost:8081/ws
```

> ⚠️ **Never commit `.env` files with real credentials.** Only `.env.example` files are tracked in version control.

---

## API Overview

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Authenticate and receive JWT | Public |
| POST | `/api/auth/register` | Register new user | Admin |
| GET | `/api/contracts` | List all contracts | All roles |
| POST | `/api/contracts` | Create a new contract | Manager+ |
| PATCH | `/api/contracts/{id}/status` | Update contract status | Manager+ |
| GET | `/api/clients` | List all clients | Staff+ |
| POST | `/api/documents/upload` | Upload document to Cloudinary | Staff+ |
| GET | `/api/audit-logs` | View full audit trail | Admin |
| GET | `/api/analytics/renewals` | Contract renewal predictions | Admin, Manager |

Full documentation available at `/swagger-ui.html` when the backend is running.

---

## Role Permissions

| Feature | Admin | Manager | Staff | Client |
|---|:---:|:---:|:---:|:---:|
| View contracts | ✅ | ✅ | ✅ | ✅ (own only) |
| Create contracts | ✅ | ✅ | ❌ | ❌ |
| Approve contracts | ✅ | ✅ | ❌ | ❌ |
| Upload documents | ✅ | ✅ | ✅ | ❌ |
| View audit logs | ✅ | ❌ | ❌ | ❌ |
| View analytics | ✅ | ✅ | ❌ | ❌ |
| Manage users | ✅ | ❌ | ❌ | ❌ |

---

## Author

**Divine Uwera**  
Software Engineering Student — Adventist University of Central Africa (AUCA), Kigali  
📧 uweradivine485@gmail.com  
🐙 [github.com/uweradivi](https://github.com/uweradivi)

---

## License

This project is built for academic and portfolio purposes.
