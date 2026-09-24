# Modernization log

## Step 1 — reproducible Java build (2026-09-12)

Scope: introduce Maven without changing the desktop application's behavior.

- `pom.xml` declares the JSON and test dependencies and preserves the existing
  `src/main` and `src/test` layout.
- The compilation target remains Java 11 for the legacy baseline; the Spring
  Boot migration will choose its Java version separately.
- JUnit Jupiter executes the tests. JUnit 4 is retained only because existing
  tests import its assertion helpers.
- Run `mvn test` from the repository root with Maven and JDK 11 or newer installed.
- Maven puts generated output in `target/`, which is ignored by Git.

Baseline observation: before adding Maven, the bundled JUnit console discovered
25 tests and all passed. This includes the two pre-existing untracked event test
files; their inclusion in a future commit must be explicit.

Known follow-ups, not fixed in this step:

- `Movie.averageScore()` performs integer division and does not handle an empty
  ratings collection.
- `EventTest` compares two separately sampled times and can fail intermittently.
- Persistence tests write to repository fixtures; isolate those writes before
  expanding the test suite.

Next increments: establish stable tests, add a Maven wrapper, configure GitHub
Actions, and then start the Spring Boot migration. Keep each increment separate
and verified. The current origin is the UBC course repository; the personal
GitHub destination is https://github.com/zjiyang/Movie-Manager-Application.

Verification: Maven successfully compiled 13 application sources and 10 test
sources; all 25 local tests passed. The existing tracked suite contains 21 tests;
the additional four belong to the two untracked event test files and are not
part of this build-configuration commit.

## Step 2 — isolate and stabilize legacy tests (2026-09-22)

### Changes

- Add the two existing local event test files to version control.
- Check event creation time against the interval surrounding construction,
  rather than requiring two separate clock reads to match exactly.
- Clear the singleton event log before and after its tests.
- Write JSON round-trip test output into JUnit-managed temporary directories,
  leaving the repository's example data untouched.
- Assert that an empty database is still empty after saving and reading it.

### Verification

- `mvn clean test`: 25 tests passed, zero failures/errors/skips.
- `git diff -- data`: no changes to the tracked data files after testing.
- No application behavior or database implementation changes in this increment.

### Next increments and working schedule

Each update gets its own focused commit, verification result and log entry.
The working target is a runnable core version over two to three work days,
not an unattended schedule or a guarantee of the full feature set.

1. Day 1: test baseline, Maven wrapper and GitHub Actions; then PostgreSQL setup
   and the first versioned movie schema.
2. Day 2: Spring Boot movie endpoints and database integration tests; introduce
   rating persistence with clear rules and test data.
3. Day 3: React browsing/rating interface, end-to-end verification and README.

Authentication, public write access and cloud deployment require their own
verified increments. Until authorization is implemented, write demonstrations
remain local with test identities. Reassess those features after the core works.

## Step 3 — Maven Wrapper and GitHub Actions (2026-09-22)

- Generate the official Apache Maven Wrapper 3.3.4 (only-script distribution),
  pinning Maven 3.9.9. Include macOS/Linux and Windows launch scripts.
- Add Java CI for pushes, pull requests and manual runs using Temurin Java 11,
  dependency caching, read-only repository permissions and a 10-minute timeout.
- Document prerequisites, local commands, report locations and the scope of CI.
- Local verification: Wrapper on Temurin Java 11, `clean test`, 25 tests passed.
- The first remote run failed during tests. Inspection found a reader fixture
  path with `DataBase` where the tracked filename uses `Database`. Correct the
  path to match Git exactly; this mismatch was hidden by local macOS filesystem
  case-insensitivity. The corrected commit `2edc3e9` passed Linux/Java 11 CI:
  https://github.com/zjiyang/Movie-Manager-Application/actions/runs/35821959355.
- No application behavior, deployment or branch-protection changes.

## Step 4 — PostgreSQL development environment (2026-09-23)

- Add Compose PostgreSQL 17 with a health check, localhost-only port and named
  volume; require a password via an ignored `.env` file.
- Bootstrap a movies table and fictional demo records on empty volumes only.
  This is not Flyway or a migration from legacy JSON.
- Add rollback-based CRUD/constraint checks and a separate CI job that replaces
  containers and verifies an inserted marker survives on the retained volume.
- Local Docker is unavailable. Remote verification passed for both Java tests
  and the PostgreSQL job (startup, CRUD/constraints, container replacement and
  retained data): https://github.com/zjiyang/Movie-Manager-Application/actions/runs/35892767115.
  Verified code commit: `53e22d9`. Java code unchanged.
- Document startup, shutdown, volume behavior and current integration limits.

## Step 5 — read-only Spring Boot movie API (2026-09-23)

- Add a separate `backend/` Maven project with Spring Boot 3.5.16, Java 21,
  Spring Web, Spring Data JPA and the PostgreSQL driver.
- Expose `GET /api/movies` (bounded pagination, stable ID ordering) and
  `GET /api/movies/{id}`; return 400 for invalid input and 404 for missing movies.
- Keep database entities separate from response records. Use read-only service
  transactions and schema validation, without automatic schema changes.
- Add real HTTP/PostgreSQL integration checks in a separate Java 21 CI job.
- Local Java 24 successfully compiled Java 21 bytecode, compiled test sources
  and packaged the executable backend. Local Docker is still unavailable;
  packaging alone is not an integration test.
- Remote verification passed: all three CI jobs (legacy Java, database checks,
  and real HTTP/PostgreSQL backend integration) succeeded for commit `0a7e951`.
  https://github.com/zjiyang/Movie-Manager-Application/actions/runs/35939434260
- Flyway adoption is intentionally separated from this read-only step; no schema
  changes, legacy JSON import, authentication or write endpoints are included.
