# Simple helper Makefile
.PHONY: docker up down dev frontend backend

docker:
	@docker compose up --build

up:
	@docker compose up

down:
	@docker compose down

dev-frontend:
	cd frontend && npm install && npm run dev

dev-backend:
	cd backend && ./mvnw spring-boot:run
