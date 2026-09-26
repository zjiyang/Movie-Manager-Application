import { useCallback, useEffect, useState } from 'react';
import { fetchConfig, fetchMovie } from '../api';
import { useAsync } from '../hooks/useAsync';
import type { Movie } from '../types';
import { RatingControl } from './RatingControl';
import { Status } from './Status';

export function MovieDetail({ id }: { id: number }) {
  const state = useAsync(useCallback(() => fetchMovie(id), [id]), [id]);
  const config = useAsync(fetchConfig, []);
  // The rating control hands back the movie the server returned, so the average
  // on screen is the server's answer rather than a number guessed on the client.
  const [movie, setMovie] = useState<Movie | null>(null);
  useEffect(() => setMovie(state.data), [state.data]);

  return (
    <>
      <p>
        <a href="#/">← All movies</a>
      </p>
      <Status state={state} />
      {movie && (
        <article className="detail">
          <h2>{movie.title}</h2>
          <p className="meta">{movie.releaseYear ?? 'Year unknown'}</p>

          <dl>
            <dt>Genres</dt>
            <dd>{movie.genres.length > 0 ? movie.genres.join(', ') : '—'}</dd>
            <dt>Where to watch</dt>
            <dd>{movie.streamServices.length > 0 ? movie.streamServices.join(', ') : '—'}</dd>
            <dt>Average</dt>
            <dd>
              {movie.averageScore === null
                ? 'Not rated yet'
                : `${movie.averageScore.toFixed(1)} / 10 from ${movie.ratingCount} ${
                    movie.ratingCount === 1 ? 'rating' : 'ratings'
                  }`}
            </dd>
          </dl>

          <RatingControl
            movie={movie}
            writable={config.data?.ratingsWritable ?? false}
            onChange={setMovie}
          />
        </article>
      )}
    </>
  );
}
