const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

export type Scenario = {
  id: string;
  title: string;
  description: string;
  category: string;
};

export type ScenarioRunResult = {
  scenarioId: string;
  environment: string;
  success: boolean;
  userMessage: string;
  rootCause: string;
  mitigation: string;
  permanentFix: string;
  prevention: string;
  debug: Record<string, unknown>;
};

export async function fetchScenarios(): Promise<Scenario[]> {
  const res = await fetch(`${API_BASE}/api/scenarios`);
  if (!res.ok) throw new Error("Failed to load scenarios");
  return res.json();
}

export async function fetchDiff(env: "DEV" | "PROD"): Promise<any> {
  const res = await fetch(`${API_BASE}/api/diff?env=${env}`);
  if (!res.ok) throw new Error("Failed to load diff");
  return res.json();
}

export async function runScenario(id: string, env: "DEV" | "PROD"): Promise<{data: ScenarioRunResult; correlationId: string | null;}> {
  const res = await fetch(`${API_BASE}/api/run/${id}?env=${env}`, { method: "POST" });
  const correlationId = res.headers.get("X-Correlation-Id");
  const data = await res.json();
  return { data, correlationId };
}
