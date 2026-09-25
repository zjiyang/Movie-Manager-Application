import type { AsyncState } from '../hooks/useAsync';

/** Shows whichever of loading or error applies, and nothing once data arrives. */
export function Status<T>({ state }: { state: AsyncState<T> }) {
  if (state.loading && state.data === null) return <p className="status">Loading…</p>;
  if (state.error) {
    return (
      <p className="status error" role="alert">
        {state.error} <button onClick={state.reload}>Try again</button>
      </p>
    );
  }
  return null;
}
