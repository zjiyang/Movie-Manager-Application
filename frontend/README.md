# Frontend

React 18 and TypeScript on Vite, talking to the Spring Boot API.

```sh
npm install
npm run dev      # http://localhost:5173
npm run build    # typecheck, then production bundle into dist/
```

`npm run dev` forwards `/api` to `http://127.0.0.1:8080`, so start the backend
first. The browser always calls the same origin it was served from, in
development and in production, which means there is no CORS configuration and no
API address baked into the bundle.

## What is here

| Path | Purpose |
| --- | --- |
| `src/api.ts` | The only place that knows the API's shape; errors become `ApiError` |
| `src/types.ts` | The response types, mirroring the backend's records |
| `src/hooks/useAsync.ts` | Loading, error and reload state for one request |
| `src/hooks/useHashRoute.ts` | Hash routing, so every view has a URL without a router dependency |
| `src/components/` | The list, the detail view and the rating control |

Two decisions worth naming:

- **Figures come from the server.** After rating, the screen shows the average
  the backend returned rather than one recomputed in the browser, so the two can
  never disagree.
- **A stale response cannot win.** `useAsync` ignores a result whose request has
  been superseded, so switching genre filters quickly leaves the right list on
  screen.

## Limit

Every rating belongs to the same demo user id, because the service has no
sign-in yet. The rating endpoints are not safe to expose publicly until it does.
