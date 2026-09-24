# Movie query API

Spring Boot 3.5.16 / Spring Web / Spring Data JPA / PostgreSQL. Target Java 21;
the original desktop project remains separate at the repository root on Java 11.

## Run locally

Requires JDK 21 or newer compatible with Spring Boot 3.5, and a running Docker
engine. Follow [database setup](../docs/database.md) first. From the repo root,
in a macOS/Linux shell, export the same password used for Compose without
putting it in shell history:

```sh
read -r -s 'POSTGRES_PASSWORD?Database password: '
export POSTGRES_PASSWORD
docker compose up -d --wait db
./mvnw -f backend/pom.xml spring-boot:run
```

The `read` prompt above is for zsh (the development Mac's shell); in bash use
`read -r -s -p 'Database password: ' POSTGRES_PASSWORD` instead.
Spring Boot does not automatically read the Compose `.env` file. Export
`POSTGRES_PORT` too if you changed the port. Stop the server with Ctrl-C.
The server binds to `127.0.0.1:8080` for local development only.

## Endpoints

| Request | Response |
| --- | --- |
| `GET /api/movies?page=0&size=20` | Paginated list ordered by movie ID |
| `GET /api/movies/{id}` | One movie, or HTTP 404 when absent |

Pages start at 0; size must be 1–100. Invalid IDs or parameters return HTTP 400.
An out-of-range page returns HTTP 200 with empty `items`.

```sh
curl 'http://127.0.0.1:8080/api/movies?page=0&size=20'
curl 'http://127.0.0.1:8080/api/movies/1'
```

List JSON contains `items`, `page`, `size`, `totalElements`, and `totalPages`.
Each movie contains `id`, `title`, `releaseYear` (nullable), and `createdAt`.
No write endpoints, authentication or frontend are implemented yet.

## Build and verify

```sh
# Compiles production AND test code, builds executable JAR; does not run the ITs.
./mvnw -f backend/pom.xml package
# Requires a disposable PostgreSQL database initialized by Compose and exported password.
./mvnw -f backend/pom.xml -Pintegration verify
```

Integration tests start a real HTTP server on a random port and use the actual
PostgreSQL database, not mocks or H2. They insert and remove their own fixture
rows and expect the two demo rows from database initialization. Do not run them
against production data. CI creates a fresh database for this job and runs the
legacy Java and standalone database checks separately.

The backend only validates the existing schema (`ddl-auto=validate`). It does
not create, update or drop tables automatically. Flyway adoption, including
handling existing volumes without losing data, is a separate upcoming step.
Legacy JSON files are not imported. This step deliberately does not alter the
schema introduced by the preceding commit.

## Verified result

Commit `0a7e951` passed all three CI jobs, including the real HTTP/PostgreSQL
integration suite on Java 21:
https://github.com/zjiyang/Movie-Manager-Application/actions/runs/35939434260.
Local compilation/packaging succeeded on Java 24 with a Java 21 target; local
database startup remains unverified because Docker is not installed on the Mac.
