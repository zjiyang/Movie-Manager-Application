# 🎬 Movie Rating and Recommendation Platform 

## Build and test

PostgreSQL development setup: see [database instructions](docs/database.md).
The desktop app still uses JSON; backend integration is a later step.

Requires JDK 11 or newer. The Maven Wrapper downloads the pinned Maven 3.9.9
version on first use; a separate Maven installation is not required. Initial
setup requires internet access to download Maven and project dependencies.

```sh
# macOS / Linux, from the repository root
./mvnw --batch-mode --no-transfer-progress clean test
```

On Windows, use `mvnw.cmd --batch-mode --no-transfer-progress clean test`.
Test reports are written to `target/surefire-reports/`.

The **Java CI** GitHub Actions workflow runs the same command on Java 11 for
pushes and pull requests, and can also be started manually from the Actions tab.
This checks compilation and automated tests; it does not deploy the application
or test the desktop GUI. A passing workflow is not a required merge check unless
repository branch protection is configured separately.

See [the Chinese project story log](docs/项目升级故事记录.md) for the reasoning
behind each upgrade and [the technical log](docs/modernization-log.md) for details.

## What can this do?
This application is all about rating and discovering movies. It helps users record their thoughts on movies they’ve seen, find highly-rated recommendations, and see where they can stream those movies. Key features include:
- Adding a new movie to the platform’s data, providing details such as title, release year, genre, and streaming platform availability.
- Rating movies on a scale of 1-5 and contributing personal reviews or notes.
- Displaying top-rated movies within various categories, ranked by their average scores.
- Checking which streaming services have a movie available.

## Who is this for?
This platform is designed for anyone who loves movies and wants an easy way to rate films, find top picks. It’s especially useful for those who value having a curated set of recommendations and quick access to streaming information.

## Why choose this project?
This project stands out because it focuses on enhancing the movie experience. It highlights highly-rated films, provides organized categories, and integrates streaming service data so users can quickly find and enjoy their favorite movies. It’s all about making the movie discovery process more enjoyable and efficient.

## User Stories
- As a user, I want to contribute a new movie to the platform by providing its title, release year, genre, and streaming platform availability.
- As a user, I want to view all the movies on the platform with name, year, and average score.
- As a user, I want to rate a movie, or change my previous rate, on a scale of 0-10.
- As a user, I want to have movie recommendation by checking the top-rated movie in a given genre.
- As a user, I want to find out which streaming services have a particular movie available.
- As a user, when I select the quit option from the application menu, I want to be reminded to save my movie contributions to the platform database and have the option to do so or not.
- As a user, when I start the application, I want to be given the option to load my saved movie contributions from the platform database.

## Instructions for End User

- You can contribute a new movie to the platform by providing its title, release year, genre, and streaming platform availability by clicking the "Add Movie" button. 
- You can add multiple movies by repeating the step above.
- You can rate a movie by using slider scaled from 0 to 10.
- You can load the data base by clicking "File", then "Load Movies" form menu.
- You can save your new added movies to the database by clicking "File", then "Save Movies" form menu.
- You can view all movies in the database, along with released year and average score infomation, in the middle of this application interface.

## Phase 4: Task 2
Some samples of Events:<br />

Thu Mar 27 10:45:58 PDT 2025 <br />
Set released year 2004 to movie Movie A<br />
Thu Mar 27 10:46:00 PDT 2025<br />
Added genre Drama to movie Movie A<br />
Thu Mar 27 10:46:00 PDT 2025<br />
Added genre Comedy to movie Movie A<br />
Thu Mar 27 10:46:12 PDT 2025<br />
User 1847223 rated Movie A with score 9<br />
Thu Mar 27 10:46:15 PDT 2025<br />
Added stream service Only In Theater to movie Movie A<br />
Thu Mar 27 10:46:15 PDT 2025<br />
Added movie: Movie A to the Movie Database.<br />

## Phase 4: Task 3
If I had more time, I would consider implementing these two refactoring improments:
- refactoring the Movie class to reduce its responsibility. Currently, it handles rating process, stream service and genre assignment, which could be delegated to separate helper classes, like RatingManager or StreamServiceManager. 
- In MovieAppGUI class, I can also refactor to reduce the coupling problem by separate a few methods, including rateMovie, setStreamService and setGenre, to a specific GUI class for each of them. 

In this way, I think it can improve the cohension and decrease the coupling problem.
