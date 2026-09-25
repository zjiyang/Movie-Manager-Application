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
