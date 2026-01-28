export default function EnvToggle(props: {
  env: "DEV" | "PROD";
  onChange: (e: "DEV" | "PROD") => void;
}) {
  const { env, onChange } = props;

  return (
    <div className="card" style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
      <div>
        <div className="h2">Environment</div>
        <div className="muted">
          Toggle DEV vs PROD to simulate “works in dev, fails in prod”.
        </div>
      </div>

      <div style={{ display: "flex", gap: 10 }}>
        <button className="btn" onClick={() => onChange("DEV")} style={{ opacity: env === "DEV" ? 1 : 0.6 }}>
          DEV
        </button>
        <button className="btn" onClick={() => onChange("PROD")} style={{ opacity: env === "PROD" ? 1 : 0.6 }}>
          PROD
        </button>
      </div>
    </div>
  );
}
