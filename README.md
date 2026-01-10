# LLM-gestuetzte-Softwareentwicklung-Labor

This repository is for the subject LLM-gestützte Softwareentwicklung at the University Hochschule Esslingen.

## Presentation

[View Presentation](https://www.canva.com/design/DAG1rFhNIwk/lLVcl3SvNAtR_1H8-zRrzQ/view?utm_content=DAG1rFhNIwk&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h4440143821)

[View 2nd Presentation](https://www.canva.com/design/DAG69rwnytM/W_r624AuQOi_cWFuCyoR3g/view?utm_content=DAG69rwnytM&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h1a1c5ac053)

## Database

Start the **Docker Desktop** Application.

Creates and starts the database and adminer (db control tool) defined in [docker-compose.yml](./docker-compose.yml) in the background (-d flag = detached mode):

```console
docker compose up -d
```

To stop the containers simply insert the following docker command, which will also delete the created container and the database files (-v flag = volumes)

```console
docker compose down -v
```

## Backend & Frontend mit Docker (vollständig containerisiert)

Startet Backend, Frontend und alle Services mit einem Befehl:

```console
docker compose up --build
```

Zugriff:
- **Frontend**: http://localhost:5173
- **Backend**: http://localhost:8080
- **Adminer (DB)**: http://localhost:8081
- **Ollama**: http://localhost:11434

Zum Beenden:

```console
docker compose down
```

## Backend (Lokal ohne Docker)

Starting backend, by changing into the backend folder:

```console
cd backend
```

Starting backend (Maven Wrapper):

```console
./mvnw spring-boot:run
```

Use .env file for secrets. See what fields to set in .env.example file

### Image Generation
For generating new images you can change the path to your model and include your API_KEY in .env file.
Before generating new images, delete images from backend folder and delete path in database. When starting backend for every dinosaur which doesnt have a img_url a new image is created.
Use the following inside pgAdmin:
```
UPDATE dinosaurs
SET image_url = NULL
WHERE id IN (example_id_1, example_id_32);
```

## Frontend (Lokal ohne Docker)

Starting frontend, by changing into the frontend folder:

```console
cd frontend
```

Installing dependencies (Only if this is your **first setup**):

```console
npm install
```

And finally starting the frontend:

```console
npm run dev
```
