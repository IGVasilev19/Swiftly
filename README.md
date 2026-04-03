# Swiftly 🚗

A comprehensive vehicle rental platform built with a robust microservices architecture. Swiftly enables users to list vehicles for rent, manage listings, and book vehicles seamlessly.

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-blue.svg)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8-blue.svg)](https://www.typescriptlang.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-CSS-38bdf8.svg)](https://tailwindcss.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

## 📖 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features
- **Role-Based Access Control:** Distinct experiences for Owners and Renters.
- **Vehicle Management:** Owners can add, update, and manage vehicle listings.
- **Booking System:** Renters can browse the catalogue and book vehicles.
- **Secure Auth:** JWT-based authentication with refresh token rotation.
- **Interactive UI:** Dynamic calendar selection and image galleries.
- **Asset Management:** Image upload handling for vehicle profiles.

## 🛠️ Tech Stack
* **Backend:** Java 17, Spring Boot, Spring Security, Hibernate, PostgreSQL (via Docker).
* **Frontend:** React 19, TypeScript, Vite, Tailwind CSS, TanStack Query, Zod.
* **Tools:** Docker, Playwright (E2E Testing), Gradle.

## 📁 Project Structure
```text
Swiftly/
├── backend/       # Spring Boot microservices (application, boot, domain, persistence, infrastructure)
├── frontend/      # React/Vite web application
└── docker-compose.yml
```

## 🚀 Installation

### Prerequisites
- Node.js (v20+)
- Java (JDK 17)
- Docker & Docker Compose

### Running Locally
1. Clone the repository.
2. Navigate to `frontend` and install dependencies:
   ```bash
   cd frontend && pnpm install
   ```
3. Start the application stack using Docker Compose from the root:
   ```bash
   docker-compose up -d
   ```

## 💡 Usage
### Owners
- Navigate to the **Dashboard** to track performance.
- Use the **Vehicles** tab to register your car with specific details (VIN, Fuel, Features).
- Click **List Vehicle** to enable renters to view your car in the catalogue.

### Renters
- Browse the **Catalogue** to filter available vehicles.
- Use the booking calendar to select a rental period.
- Manage your history via the **Bookings** tab.
