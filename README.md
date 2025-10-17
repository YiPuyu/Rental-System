```markdown
# Shangting Apartment Project

A full-stack apartment management system built with **Spring Boot** (backend), **React + Vite** (frontend), and a **Python ML service** for rent prediction. Demonstrates JWT authentication, role-based access, REST APIs, and full-stack integration.

---

## 🧩 Tech Stack

**Backend:**
- Java 17 + Spring Boot 3.2.4
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Swagger3 for API documentation

**Frontend:**
- React 19 + Vite
- ESLint for code quality
- React Hooks for state management

**ML Service:**
- Python 3.12
- scikit-learn
- pandas, numpy
- FastAPI for serving rent prediction model
- joblib for model persistence

---

## 🔑 Key Features

### Authentication & Authorization
- JWT-based login & registration
- Role-based access:
  - **ADMIN** → full access
  - **LANDLORD / TENANT** → property access
- Secure password storage with **BCrypt**

### Property Management
- CRUD operations on properties
- REST API endpoints for frontend
- Pagination and filtering (planned)

### Rent Prediction
- Enter city and house type to predict rent
- Served via Python FastAPI (`ml-service`)
- Integrated with Spring Boot backend for seamless use

### Admin Management
- Admin dashboard for managing users & properties
- Role-protected API endpoints

### Security
- Global CORS configured for React frontend (`http://localhost:5173`)
- CSRF disabled for API testing
- JWT filter integrated into Spring Security

---

## ⚙️ Project Structure

```

shangting/
├─ backend/ (Spring Boot)
│  ├─ src/
│  ├─ pom.xml
│  └─ target/
├─ frontend/ (React + Vite)
│  ├─ src/
│  ├─ package.json
│  ├─ package-lock.json
│  └─ vite.config.js
├─ ml-service/ (Python ML API)
│  ├─ rent_model.pkl
│  ├─ rent_encoder.pkl
│  ├─ api.py
│  └─ requirements.txt
└─ README.md

````

---

## 🚀 Run Locally

### Backend
1. Configure **MySQL** in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shangting
spring.datasource.username=root
spring.datasource.password=your_password
````

2. Build & run Spring Boot:

```bash
mvn clean install
mvn spring-boot:run
```

### Frontend

1. Install dependencies:

```bash
npm install
```

2. Start Vite dev server:

```bash
npm run dev
```

3. Open in browser: [http://localhost:5173](http://localhost:5173)

### ML Service

1. Install dependencies:

```bash
pip install -r ml-service/requirements.txt
```

2. Start FastAPI server:

```bash
uvicorn ml-service.api:app --reload
```

3. API endpoint example: POST `/predict` with JSON `{ "city": "Tokyo", "houseType": "Apartment" }`

---

## 📈 Highlights

* Full-stack integration with JWT security
* Role-based authorization
* REST API and ML service ready for production
* Swagger UI for backend API testing
* Python ML service predicts rent dynamically
* Modern frontend tooling with React + Vite

---

## 🔮 Future Improvements

* Image upload for property listings
* Search, filter, and pagination on frontend
* Unit & integration tests
* Deploy on Hugging Face Spaces, Heroku, or cloud service

```

