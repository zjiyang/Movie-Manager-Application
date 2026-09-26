# Running and deploying the whole thing

## One image, one origin

The `Dockerfile` builds the browser bundle, drops it inside the executable jar,
and runs the jar on a JRE as a non-root user. One container serves both the page
and the API, so the browser always calls the origin it was loaded from. That is
why there is no CORS configuration in this project and no API address compiled
into the bundle.

Routing lives in the URL hash, so a deep link like `/#/movies/3` never reaches
the server as a path. No rewrite rules are needed on whatever hosts this.

## Locally

```sh
cp .env.example .env      # choose a password
docker compose up --build
```

Then open <http://127.0.0.1:8080>. `APP_PORT` in `.env` moves it if 8080 is busy.
The application applies its migrations on startup, so the database starts empty
and ends up with the imported library.

To work on the frontend with hot reload, run the backend on its own and start
Vite beside it; Vite forwards `/api` to port 8080:

```sh
docker compose up -d --wait db
./mvnw -f backend/pom.xml spring-boot:run
cd frontend && npm run dev          # http://localhost:5173
```

## Deploying

`render.yaml` describes a web service built from the `Dockerfile` plus a managed
PostgreSQL instance, and wires the database's host, port, name, user and
password into the environment the application already reads. Keeping this in the
repository means the deployment is reviewable and repeatable instead of a set of
remembered form fields.

Check the current plans before applying it. Plan names and free-tier terms
change, and a managed database in particular may expire or start costing money.

The same image runs anywhere that accepts a Dockerfile. What a host must provide:

| Variable | Why |
| --- | --- |
| `SERVER_ADDRESS=0.0.0.0` | A published port cannot reach a process bound to loopback |
| `PORT` | Only if the host assigns one; it defaults to 8080 |
| `POSTGRES_HOST` / `PORT` / `DB` / `USER` / `PASSWORD` | The database |
| `RATINGS_WRITABLE=false` | See below |

`/actuator/health` is the health check. It is the only management endpoint
exposed, and it reports no details.

## Writes are closed in public deployments

The rating endpoints still take the user from the URL, so anyone who can reach
them can write as anyone. Until authentication replaces that, a deployment sets
`RATINGS_WRITABLE=false`: `PUT` and `DELETE` answer 403, and the page says so
instead of offering buttons that would be refused.

This is a switch, not security. It is here so the service can be public before
sign-in exists, and the switch goes away in the same step that adds sign-in.

## What CI checks

A job builds the image, starts it against a disposable database, and then
verifies against the running container that the health check reports `UP`, the
page is served, the API returns the imported library and the genre vocabulary,
`/api/config` reports the closed state, and a `PUT` to a rating really is
refused with 403. This is the first place the frontend, the backend and the
database run together.
