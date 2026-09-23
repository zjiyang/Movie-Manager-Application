# PostgreSQL local development

This step adds a standalone development database. The Java desktop application
still reads/writes JSON; no existing movie data has been migrated.

## Start

Install and start Docker Desktop (or another Docker engine with Compose v2).
From the repository root:

```sh
cp .env.example .env
# Edit .env and replace the example password before starting.
docker compose up -d --wait db
docker compose exec db psql -U movie_app -d movie_manager
```

In psql: `SELECT * FROM movies;` lists two fictional demo movies. Use `\q` to exit.
Database: `movie_manager`; user: `movie_app`; host: `127.0.0.1`; port: `5432`.
Set `POSTGRES_PORT` in `.env` if that port is already in use. Passwords in `.env`
are ignored by Git. The database is bound only to the local machine.
This bootstrap account owns the database; it is not a production permissions design.

## Check and stop

```sh
docker compose exec -T db psql -U movie_app -d movie_manager -v ON_ERROR_STOP=1 < database/checks.sql
docker compose down
```

The check rolls back its rows (identity sequences can still advance). `down`
removes containers but retains the named `movie_data` volume; start again to
reuse the data. Do not add `--volumes` unless you intend to erase this database.
CI uses that deletion flag only for its own disposable, uniquely named project.

## Schema choices and limits

- `movies`: generated ID, nonblank title (up to 255 characters), optional release
  year between 1888 and 2100, and creation timestamp.
- Titles are not unique: different films can share a name. Unknown years use
  SQL NULL rather than the desktop model's default zero.
- Users, ratings and genres are later steps.
- `database/init/001_create_movies.sql` runs only when the database volume is
  first created. Editing it does NOT update an existing database. Flyway will
  be introduced with Spring Boot, with an explicit plan for this existing schema.
- The PostgreSQL 17 major version is pinned; patch releases may change the image.

## Verification status

The development Mac has no Docker installation detected. Local startup is not
yet verified. GitHub CI will validate this exact Compose configuration, exercise
CRUD and invalid-input constraints, and check data survives container replacement.
