import type { ScenarioRunResult } from "../api/client";

export default function ResultPanel(props: {
  result: ScenarioRunResult | null;
  correlationId: string | null;
  diff: any;
}) {
  const { result, correlationId, diff } = props;

  if (!result) {
    return (
      <div className="card">
        <div className="h2">Result</div>
        <div className="muted">Run a scenario to see logs, root cause, fix and prevention steps.</div>
      </div>
    );
  }

  return (
    <div className="card">
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: 10 }}>
        <div className="h2">Result ({result.environment})</div>
        <span className="pill" style={{ borderColor: result.success ? "#2a8256" : "#b54b4b" }}>
          {result.success ? "SUCCESS" : "FAILURE"}
        </span>
      </div>

      <p style={{ marginTop: 8 }}><b>User-facing:</b> {result.userMessage}</p>
      {correlationId && <p className="muted"><b>Correlation ID:</b> {correlationId}</p>}

      <div className="grid2" style={{ marginTop: 12 }}>
        <div>
          <div className="h2">Root Cause</div>
          <p className="muted">{result.rootCause}</p>

          <div className="h2">Mitigation</div>
          <p className="muted">{result.mitigation}</p>

          <div className="h2">Permanent Fix</div>
          <p className="muted">{result.permanentFix}</p>

          <div className="h2">Prevention</div>
          <p className="muted">{result.prevention}</p>
        </div>

        <div>
          <div className="h2">Debug (Simulated Logs/Trace)</div>
          <pre>{JSON.stringify(result.debug, null, 2)}</pre>

          <div className="h2">Dev vs Prod Diff</div>
          <pre>{JSON.stringify(diff, null, 2)}</pre>
        </div>
      </div>
    </div>
  );
}
