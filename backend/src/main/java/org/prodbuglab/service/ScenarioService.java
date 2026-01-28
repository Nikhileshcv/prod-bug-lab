package org.prodbuglab.service;

import org.prodbuglab.model.Scenario;
import org.prodbuglab.model.ScenarioRunResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScenarioService {
    private static final Logger log = LoggerFactory.getLogger(ScenarioService.class);
    private final Environment springEnv;

    public ScenarioService(Environment springEnv) {
        this.springEnv = springEnv;
    }

    public List<Scenario> list() {
        return List.of(
                new Scenario("db-auth", "DB Authentication Fails (Prod Only)",
                        "DEV works. PROD fails with password authentication error due to secret/config drift.", "Config Drift"),
                new Scenario("route-mismatch", "Gateway Route Mismatch (404 in Prod)",
                        "DEV hits /api/orders. PROD expects /api/v1/orders behind gateway → 404.", "Infrastructure"),
                new Scenario("cors-like", "CORS/Security Block (Browser Fails in Prod)",
                        "DEV allows requests. PROD blocks due to stricter origin/security policy (simulated).", "Security")
        );
    }

    public Map<String, Object> diff(String uiEnv) {
        // This is a "visual diff" payload the frontend can render.
        // In real life you'd show k8s configmaps/secrets, env vars, gateway routes, etc.
        Map<String, Object> dev = Map.of(
                "apiBasePath", "/api",
                "ordersRoute", "/api/orders",
                "dbUser", "dev_user",
                "dbPasswordSource", "local .env",
                "featureFlag_NEW_CHECKOUT", false
        );
        Map<String, Object> prod = Map.of(
                "apiBasePath", "/api",
                "ordersRoute", "/api/v1/orders",
                "dbUser", "orders",
                "dbPasswordSource", "k8s secret / parameter store",
                "featureFlag_NEW_CHECKOUT", true
        );

        return Map.of(
                "selectedEnv", uiEnv,
                "dev", dev,
                "prod", prod
        );
    }

    public ScenarioRunResult run(String scenarioId, String uiEnv) {
        String effectiveEnv = (uiEnv == null || uiEnv.isBlank()) ? "DEV" : uiEnv.toUpperCase();

        // Spring profile is about runtime; UI env is the user toggle for the simulator.
        String springProfiles = String.join(",", springEnv.getActiveProfiles());
        log.info("Run scenario={} uiEnv={} springProfiles={} at={}", scenarioId, effectiveEnv, springProfiles, Instant.now());

        return switch (scenarioId) {
            case "db-auth" -> runDbAuth(effectiveEnv);
            case "route-mismatch" -> runRouteMismatch(effectiveEnv);
            case "cors-like" -> runCorsLike(effectiveEnv);
            default -> new ScenarioRunResult(
                    scenarioId, effectiveEnv, false,
                    "Unknown scenario.", "Invalid scenario id.", "N/A", "N/A", "N/A",
                    Map.of("hint", "Use /api/scenarios to list valid ids")
            );
        };
    }

    private ScenarioRunResult runDbAuth(String env) {
        boolean prod = "PROD".equals(env);

        if (!prod) {
            Map<String, Object> debug = Map.of(
                    "dbHost", "localhost",
                    "dbUser", "dev_user",
                    "connection", "SUCCESS"
            );
            log.info("db-auth success in DEV");
            return new ScenarioRunResult(
                    "db-auth", env, true,
                    "Order created successfully (DEV).",
                    "N/A (DEV config matches secrets).",
                    "N/A",
                    "N/A",
                    "Add a prod-like integration test stage + validate required env vars at startup.",
                    debug
            );
        }

        // Simulated prod failure
        Map<String, Object> debug = new HashMap<>();
        debug.put("dbHost", "prod-db.internal");
        debug.put("dbUser", "orders");
        debug.put("error", "FATAL: password authentication failed for user \"orders\"");
        debug.put("likelyCause", "Wrong secret / rotated password not updated / mismatched username");
        log.error("db-auth failed in PROD: {}", debug.get("error"));

        return new ScenarioRunResult(
                "db-auth", env, false,
                "500 Error: Database connection failed in PROD.",
                "Config drift / wrong secret: password authentication failed for user \"orders\".",
                "Mitigate: rollback latest deploy OR swap to last-known-good secret and restart pods.",
                "Fix: update secret in parameter store/k8s, confirm username/password, redeploy.",
                "Prevent: add startup DB connectivity health check + CI config validation + canary deploy.",
                debug
        );
    }

    private ScenarioRunResult runRouteMismatch(String env) {
        boolean prod = "PROD".equals(env);

        if (!prod) {
            log.info("route-mismatch DEV path ok");
            return new ScenarioRunResult(
                    "route-mismatch", env, true,
                    "200 OK: /api/orders reachable in DEV.",
                    "N/A",
                    "N/A",
                    "N/A",
                    "Prevent: contract tests between gateway and service + smoke tests post-deploy.",
                    Map.of("expectedPath", "/api/orders", "actualPath", "/api/orders")
            );
        }

        log.warn("route-mismatch PROD 404 due to gateway path");
        return new ScenarioRunResult(
                "route-mismatch", env, false,
                "404 Not Found in PROD (simulated gateway mismatch).",
                "Ingress/API Gateway routes to /api/v1/orders but service expects /api/orders.",
                "Mitigate: route a temporary rewrite rule at gateway OR rollback gateway config.",
                "Fix: align gateway route + service base path; verify with smoke tests.",
                "Prevent: versioned routing strategy + automated gateway contract tests.",
                Map.of("gatewayRoute", "/api/v1/orders", "serviceRoute", "/api/orders")
        );
    }

    private ScenarioRunResult runCorsLike(String env) {
        boolean prod = "PROD".equals(env);

        if (!prod) {
            log.info("cors-like DEV ok");
            return new ScenarioRunResult(
                    "cors-like", env, true,
                    "Request allowed in DEV.",
                    "N/A",
                    "N/A",
                    "N/A",
                    "Prevent: security regression tests + environment parity for CORS policies.",
                    Map.of("origin", "http://localhost:5173", "policy", "allow-all (dev)")
            );
        }

        log.warn("cors-like PROD blocked (simulated)");
        return new ScenarioRunResult(
                "cors-like", env, false,
                "Browser blocked request in PROD (simulated).",
                "PROD has stricter origin/security policy; frontend origin not whitelisted.",
                "Mitigate: temporarily add the frontend domain to allowed origins (fast change).",
                "Fix: configure CORS per environment + proper allowed origins list.",
                "Prevent: keep env parity + add e2e tests from real deployed frontend domain.",
                Map.of("origin", "https://your-frontend.vercel.app", "policy", "allowlist (prod)", "allowed", false)
        );
    }
}
