# LLM-gestuetzte-Softwareentwicklung-Labor

This repository is for the subject LLM-gestützte Softwareentwicklung at the University Hochschule Esslingen.

## Presentation

[View Presentation](https://www.canva.com/design/DAG1rFhNIwk/lLVcl3SvNAtR_1H8-zRrzQ/view?utm_content=DAG1rFhNIwk&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h4440143821)

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

## Backend

Starting backend, by changing into the backend folder:

```console
cd backend
```

Starting backend (Maven Wrapper):

```console
./mvnw spring-boot:run
```

## Frontend

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
