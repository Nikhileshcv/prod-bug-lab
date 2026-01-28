import type { Scenario } from "../api/client";

export default function ScenarioCard(props: {
  scenario: Scenario;
  onRun: (id: string) => void;
}) {
  const { scenario, onRun } = props;

  return (
    <div className="card" style={{ flex: "1 1 320px" }}>
      <div style={{ display: "flex", justifyContent: "space-between", gap: 10, alignItems: "center" }}>
        <div style={{ fontWeight: 700 }}>{scenario.title}</div>
        <span className="pill">{scenario.category}</span>
      </div>
      <p className="muted">{scenario.description}</p>
      <button className="btn" onClick={() => onRun(scenario.id)}>Run</button>
    </div>
  );
}
