package org.prodbuglab.model;

import java.util.Map;

public record ScenarioRunResult(
        String scenarioId,
        String environment,
        boolean success,
        String userMessage,
        String rootCause,
        String mitigation,
        String permanentFix,
        String prevention,
        Map<String, Object> debug
) {}
