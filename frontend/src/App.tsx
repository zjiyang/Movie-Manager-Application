import { MovieDetail } from './components/MovieDetail';
import { MovieList } from './components/MovieList';
import { useHashRoute } from './hooks/useHashRoute';

export function App() {
  const route = useHashRoute();

  return (
    <div className="app">
      <header>
        <h1>
          <a href="#/">Movie Manager</a>
        </h1>
        <p className="subtitle">
          A CPSC 210 desktop application, rebuilt as a Spring Boot service over PostgreSQL.
        </p>
      </header>
      <main>{route.name === 'detail' ? <MovieDetail id={route.id} /> : <MovieList />}</main>
    </div>
  );
}
