import type { Movie, MoviePage } from './types';

/**
 * Until authentication exists, the backend takes the user from the URL and the
 * frontend has to pick one. Every rating in this build belongs to this id.
 */
export const DEMO_USER_ID = 1;

export class ApiError extends Error {
  constructor(readonly status: number, message: string) {
    super(message);
    this.name = 'ApiError';
  }
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`/api${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  });

  if (!response.ok) {
    // The backend answers errors as RFC 7807 problem details; fall back to the
    // status line when the body is not JSON.
    let detail = `Request failed with status ${response.status}`;
    try {
      const body = (await response.json()) as { detail?: string };
      if (body.detail) detail = body.detail;
    } catch {
      /* keep the fallback */
    }
    throw new ApiError(response.status, detail);
  }

  if (response.status === 204) return undefined as T;
  return (await response.json()) as T;
}

export function fetchMovies(page: number, size: number, genre: string | null): Promise<MoviePage> {
  const params = new URLSearchParams({ page: String(page), size: String(size) });
  if (genre) params.set('genre', genre);
  return request<MoviePage>(`/movies?${params}`);
}

export function fetchMovie(id: number): Promise<Movie> {
  return request<Movie>(`/movies/${id}`);
}

export function fetchGenres(): Promise<string[]> {
  return request<string[]>('/genres');
}

export function rateMovie(id: number, score: number): Promise<Movie> {
  return request<Movie>(`/movies/${id}/ratings/${DEMO_USER_ID}`, {
    method: 'PUT',
    body: JSON.stringify({ score }),
  });
}

export function withdrawRating(id: number): Promise<void> {
  return request<void>(`/movies/${id}/ratings/${DEMO_USER_ID}`, { method: 'DELETE' });
}
