export interface Movie {
  id: number;
  title: string;
  releaseYear: number | null;
  genres: string[];
  streamServices: string[];
  averageScore: number | null;
  ratingCount: number;
  createdAt: string;
}

export interface MoviePage {
  items: Movie[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ClientConfig {
  /** False in a deployment that has not enabled writes, so the UI hides them. */
  ratingsWritable: boolean;
}
