import type { Movie } from '../types';

export function MovieCard({ movie }: { movie: Movie }) {
  return (
    <li className="card">
      <a href={`#/movies/${movie.id}`}>
        <h2>{movie.title}</h2>
      </a>
      <p className="meta">
        {movie.releaseYear ?? 'Year unknown'}
        {movie.genres.length > 0 && <> · {movie.genres.join(', ')}</>}
      </p>
      <p className="score">
        {movie.averageScore === null ? (
          <span className="muted">Not rated yet</span>
        ) : (
          <>
            <strong>{movie.averageScore.toFixed(1)}</strong>
            <span className="muted">
              {' '}
              / 10 from {movie.ratingCount} {movie.ratingCount === 1 ? 'rating' : 'ratings'}
            </span>
          </>
        )}
      </p>
    </li>
  );
}
