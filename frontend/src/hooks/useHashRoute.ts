import { useEffect, useState } from 'react';

export type Route = { name: 'list' } | { name: 'detail'; id: number };

function parse(hash: string): Route {
  const match = /^#\/movies\/(\d+)$/.exec(hash);
  if (match && match[1]) return { name: 'detail', id: Number(match[1]) };
  return { name: 'list' };
}

/**
 * Routing in the hash keeps every view addressable without a router dependency
 * or any server-side rewrite rules once this is deployed.
 */
export function useHashRoute(): Route {
  const [route, setRoute] = useState<Route>(() => parse(window.location.hash));

  useEffect(() => {
    const onChange = () => setRoute(parse(window.location.hash));
    window.addEventListener('hashchange', onChange);
    return () => window.removeEventListener('hashchange', onChange);
  }, []);

  return route;
}

export function go(route: Route): void {
  window.location.hash = route.name === 'detail' ? `#/movies/${route.id}` : '#/';
}
