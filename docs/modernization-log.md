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
