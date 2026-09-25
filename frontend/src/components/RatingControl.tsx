import { useState } from 'react';
import { fetchMovie, rateMovie, withdrawRating } from '../api';
import type { Movie } from '../types';

const SCORES = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

/**
 * Setting a score is a PUT, so pressing the same button twice is harmless and
 * pressing a different one replaces the previous score rather than adding one.
 */
export function RatingControl({
  movie,
  onChange,
}: {
  movie: Movie;
  onChange: (movie: Movie) => void;
}) {
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function save(action: () => Promise<Movie | void>) {
    setBusy(true);
    setError(null);
    try {
      // Rating returns the updated movie; withdrawing returns nothing, so the
      // fresh figures come from a read. Either way the average on screen is the
      // server's answer, never a number recomputed on the client.
      const updated = (await action()) ?? (await fetchMovie(movie.id));
      onChange(updated);
    } catch (cause: unknown) {
      setError(cause instanceof Error ? cause.message : 'Could not save the rating');
    } finally {
      setBusy(false);
    }
  }

  return (
    <section className="rating">
      <h3>Your rating</h3>
      <div className="scores">
        {SCORES.map((score) => (
          <button key={score} disabled={busy} onClick={() => save(() => rateMovie(movie.id, score))}>
            {score}
          </button>
        ))}
      </div>
      <button className="link" disabled={busy} onClick={() => save(() => withdrawRating(movie.id))}>
        Remove my rating
      </button>
      {error && (
        <p className="status error" role="alert">
          {error}
        </p>
      )}
      <p className="muted small">
        Every rating in this build belongs to the same demo user, because the service has no sign-in
        yet.
      </p>
    </section>
  );
}
