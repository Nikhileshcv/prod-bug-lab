import { useEffect, useState } from "react";
import EnvToggle from "../components/EnvToggle";
import ScenarioCard from "../components/ScenarioCard";
import ResultPanel from "../components/ResultPanel";
import { fetchDiff, fetchScenarios, runScenario, type Scenario, type ScenarioRunResult } from "../api/client";

export default function Dashboard() {
  const [env, setEnv] = useState<"DEV" | "PROD">("DEV");
  const [scenarios, setScenarios] = useState<Scenario[]>([]);
  const [result, setResult] = useState<ScenarioRunResult | null>(null);
  const [correlationId, setCorrelationId] = useState<string | null>(null);
  const [diff, setDiff] = useState<any>(null);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setError(null);
    try {
      const [s, d] = await Promise.all([fetchScenarios(), fetchDiff(env)]);
      setScenarios(s);
      setDiff(d);
    } catch (e: any) {
      setError(e?.message || "Failed to load");
    }
  }

  useEffect(() => { load(); }, [env]);

  async function onRun(id: string) {
    setError(null);
    try {
      const { data, correlationId } = await runScenario(id, env);
      setResult(data);
      setCorrelationId(correlationId);
      const d = await fetchDiff(env);
      setDiff(d);
    } catch (e: any) {
      setError(e?.message || "Run failed");
    }
  }

  return (
    <>
      <div className="h1">ProdBug Lab</div>
      <div className="muted" style={{ marginBottom: 14 }}>
        Interactive demo: reproduce prod-only failures, inspect diffs/logs, and apply mitigation → fix → prevention.
      </div>

      <EnvToggle env={env} onChange={(v) => { setEnv(v); setResult(null); setCorrelationId(null); }} />

      {error && (
        <div className="card" style={{ marginTop: 16, borderColor: "#b54b4b" }}>
          <b>Error:</b> {error}
        </div>
      )}

      <div style={{ marginTop: 16 }} className="row">
        {scenarios.map((s) => (
          <ScenarioCard key={s.id} scenario={s} onRun={onRun} />
        ))}
      </div>

      <div style={{ marginTop: 16 }}>
        <ResultPanel result={result} correlationId={correlationId} diff={diff} />
      </div>

      <div className="muted" style={{ marginTop: 14 }}>
        Tip: Deploy backend on Render/Railway and frontend on Vercel. Set Vercel env var <b>VITE_API_BASE</b> to backend URL.
      </div>
    </>
  );
}
