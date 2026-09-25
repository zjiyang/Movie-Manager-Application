import { useCallback, useState } from 'react';
import { fetchGenres, fetchMovies } from '../api';
import { useAsync } from '../hooks/useAsync';
import { MovieCard } from './MovieCard';
import { Status } from './Status';

const PAGE_SIZE = 12;

export function MovieList() {
  const [genre, setGenre] = useState<string | null>(null);
  const [page, setPage] = useState(0);

  const genres = useAsync(fetchGenres, []);
  const movies = useAsync(
    useCallback(() => fetchMovies(page, PAGE_SIZE, genre), [page, genre]),
    [page, genre],
  );

  function chooseGenre(next: string | null) {
    setGenre(next);
    setPage(0);
  }

  return (
    <>
      <nav className="filters" aria-label="Filter by genre">
        <button className={genre === null ? 'chip active' : 'chip'} onClick={() => chooseGenre(null)}>
          All
        </button>
        {(genres.data ?? []).map((name) => (
          <button
            key={name}
            className={genre === name ? 'chip active' : 'chip'}
            onClick={() => chooseGenre(name)}
          >
            {name}
          </button>
        ))}
      </nav>

      <Status state={movies} />

      {movies.data && (
        <>
          <p className="count">
            {movies.data.totalElements} {movies.data.totalElements === 1 ? 'movie' : 'movies'}
            {genre ? ` in ${genre}` : ''}
          </p>
          <ul className="grid">
            {movies.data.items.map((movie) => (
              <MovieCard key={movie.id} movie={movie} />
            ))}
          </ul>
          {movies.data.totalPages > 1 && (
            <div className="pager">
              <button disabled={page === 0} onClick={() => setPage(page - 1)}>
                Previous
              </button>
              <span>
                Page {page + 1} of {movies.data.totalPages}
              </span>
              <button
                disabled={page + 1 >= movies.data.totalPages}
                onClick={() => setPage(page + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </>
  );
}
