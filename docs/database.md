# PostgreSQL development database

The schema and the imported movie library are managed by Flyway migrations in
`backend/src/main/resources/db/migration`. The Spring Boot backend runs them on
startup; Hibernate is set to `validate`, so it checks the mapping against what
Flyway built and is never allowed to create or alter a table itself.

## Start

Install and start Docker Desktop (or another Docker engine with Compose v2).
From the repository root:

```sh
cp .env.example .env
# Edit .env and replace the example password before starting.
docker compose up -d --wait db
```

The container now starts empty. Running the backend applies every migration:

```sh
./mvnw -f backend/pom.xml spring-boot:run
```

Then `docker compose exec db psql -U movie_app -d movie_manager` and
`SELECT title, release_year FROM movies;` lists the imported library. `\q` exits.

Database: `movie_manager`; user: `movie_app`; host: `127.0.0.1`; port: `5432`.
Set `POSTGRES_PORT` in `.env` if that port is already in use. Passwords in `.env`
are ignored by Git, and the database is bound only to the local machine. This
bootstrap account owns the database; it is not a production permissions design.

**Upgrading an existing local volume:** volumes created before Flyway already
contain a `movies` table, which makes the first migration fail. Those volumes are
disposable development data, so recreate them with `docker compose down --volumes`
and start again.

## Check and stop

```sh
docker compose exec -T db psql -U movie_app -d movie_manager -v ON_ERROR_STOP=1 < database/checks.sql
docker compose down
```

The check rolls back its rows (identity sequences can still advance). `down`
removes containers but retains the named `movie_data` volume; start again to
reuse the data. Do not add `--volumes` unless you intend to erase this database.
CI uses that deletion flag only for its own disposable, uniquely named project.

## Schema

| Table | Holds |
| --- | --- |
| `movies` | Generated ID, nonblank title, optional release year (1888–2100), creation timestamp |
| `genres` | The fixed vocabulary the desktop model defined, names unique |
| `stream_services` | Where a movie can be watched, names unique |
| `movie_genres` | Movie-to-genre pairs; the pair is the primary key |
| `movie_stream_services` | Movie-to-service pairs; the pair is the primary key |
| `ratings` | One score per user per movie, 0–10, unique on `(movie_id, user_id)` |

Choices worth naming:

- **A movie has many genres and a genre has many movies**, so the link lives in
  its own table rather than in a column. Making the pair the primary key means
  the database, not application code, refuses a duplicate link.
- **One rating per user per movie is a database constraint.** Two simultaneous
  requests cannot both insert; the second fails on the unique index.
- **Deleting a movie cascades** to its ratings and its links, so no orphan rows
  survive. Deleting a genre that is still in use is refused instead.
- **Titles are not unique**: different films can share a name. Unknown years are
  SQL NULL rather than the desktop model's default zero.
- `V3` imports `data/MovieDataBase.json`. `Only In Theater` is not a streaming
  platform, but the desktop application stored it in the same field, so the
  import keeps it rather than dropping data.
- The PostgreSQL 17 major version is pinned; patch releases may change the image.

## Migrations

Files are `V<version>__<description>.sql` and are applied in version order, once
each, tracked in a `flyway_schema_history` table. **A migration that has run
anywhere is never edited**; a change means a new file with the next version.

CI applies the same files with `psql` in its database job, and the backend job
exercises Flyway itself by starting the application against an empty database.

## Verification status

This development machine has no Docker installation and no Maven, so startup is
verified remotely. GitHub CI validates the Compose configuration, applies every
migration, exercises the constraints above, confirms data survives container
replacement, and starts the backend against an empty database. All three jobs
passed for commit `c2b04f4`:
https://github.com/zjiyang/Movie-Manager-Application/actions/runs/36172015928
