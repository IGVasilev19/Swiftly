import { useState } from "react";
import { fetchHealth, type HealthResponse } from "./api";

type State =
  | { kind: "idle" }
  | { kind: "loading" }
  | { kind: "ok"; data: HealthResponse; latency: number; at: string }
  | { kind: "error"; message: string; at: string };

const ProxyHint = () => (
  <div className="rounded-md border border-sky-300/50 bg-sky-50/50 p-3 text-sky-900 dark:border-sky-900/40 dark:bg-sky-900/20 dark:text-sky-100">
    <div className="text-sm leading-5">
      <span className="font-semibold">Dev proxy:</span>{" "}
      <code className="rounded bg-black/10 px-1 py-0.5 dark:bg-white/10">
        /api → http://localhost:8080
      </code>
      <span className="ml-2 opacity-80">
        (configured in <code>vite.config.ts</code>)
      </span>
    </div>
  </div>
);

export default function App() {
  const [state, setState] = useState<State>({ kind: "idle" });

  const ping = async () => {
    try {
      setState({ kind: "loading" });
      const data = await fetchHealth();
      const latency = (data as any).__latencyMs as number;
      setState({
        kind: "ok",
        data,
        latency,
        at: new Date().toLocaleTimeString(),
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : "Unknown error";
      setState({
        kind: "error",
        message: msg,
        at: new Date().toLocaleTimeString(),
      });
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 to-white px-6 py-10 text-slate-900 dark:from-slate-950 dark:to-slate-900 dark:text-slate-100">
      <div className="mx-auto max-w-2xl space-y-6">
        <header className="space-y-2">
          <h1 className="text-2xl font-bold tracking-tight">
            Frontend ↔ Backend ping
          </h1>
        </header>

        <div className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-center gap-2">
              <span className="inline-flex h-2.5 w-2.5 rounded-full bg-emerald-500 shadow-[0_0_10px] shadow-emerald-400" />
              <span className="text-sm font-medium">Backend</span>
            </div>
            <button
              onClick={ping}
              disabled={state.kind === "loading"}
              className="inline-flex items-center gap-2 rounded-lg bg-emerald-600 px-4 py-2 text-sm font-semibold text-white
                         hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {state.kind === "loading" ? (
                <>
                  <svg className="h-4 w-4 animate-spin" viewBox="0 0 24 24">
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                      fill="none"
                    />
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
                    />
                  </svg>
                  Pinging…
                </>
              ) : (
                "Ping backend"
              )}
            </button>
          </div>

          <div className="mt-4 grid gap-3">
            {state.kind === "idle" && (
              <pre className="rounded-lg bg-slate-900 p-4 text-sm text-emerald-400 shadow-inner">
                (click the button)
              </pre>
            )}

            {state.kind === "loading" && (
              <pre className="rounded-lg bg-slate-900 p-4 text-sm text-sky-300 shadow-inner">
                Sending request…
              </pre>
            )}

            {state.kind === "ok" && (
              <>
                <div className="text-sm text-slate-700 dark:text-slate-300">
                  <span className="mr-2 rounded-full bg-emerald-100 px-2 py-0.5 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-200">
                    200 OK
                  </span>
                  <span className="rounded-full bg-slate-100 px-2 py-0.5 text-slate-800 dark:bg-slate-800 dark:text-slate-100">
                    {state.latency.toFixed(0)} ms • {state.at}
                  </span>
                </div>
                <pre className="rounded-lg bg-slate-900 p-4 text-sm text-emerald-300 shadow-inner">
                  {JSON.stringify(state.data, null, 2)}
                </pre>
              </>
            )}

            {state.kind === "error" && (
              <>
                <div className="text-sm">
                  <span className="mr-2 rounded-full bg-rose-100 px-2 py-0.5 text-rose-800 dark:bg-rose-900/40 dark:text-rose-200">
                    Error
                  </span>
                  <span className="rounded-full bg-slate-100 px-2 py-0.5 text-slate-800 dark:bg-slate-800 dark:text-slate-100">
                    {state.at}
                  </span>
                </div>
                <pre className="rounded-lg bg-slate-900 p-4 text-sm text-rose-300 shadow-inner">
                  {state.message}
                </pre>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
