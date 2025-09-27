export type HealthResponse = { status: string; time: string };

export async function fetchHealth(): Promise<HealthResponse> {
  const t0 = performance.now();
  const res = await fetch("/api/health");
  const t1 = performance.now();

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(
      `HTTP ${res.status} ${res.statusText} ${text ? `– ${text}` : ""}`
    );
  }
  const json = (await res.json()) as HealthResponse;
  Object.defineProperty(json, "__latencyMs", {
    value: t1 - t0,
    enumerable: false,
  });
  return json;
}
