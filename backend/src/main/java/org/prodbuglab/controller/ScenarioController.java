package org.prodbuglab.controller;

import jakarta.validation.constraints.Pattern;
import org.prodbuglab.model.Scenario;
import org.prodbuglab.model.ScenarioRunResult;
import org.prodbuglab.service.ScenarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScenarioController {
    private final ScenarioService service;

    public ScenarioController(ScenarioService service) {
        this.service = service;
    }

    @GetMapping("/scenarios")
    public List<Scenario> scenarios() {
        return service.list();
    }

    @GetMapping("/diff")
    public Map<String, Object> diff(
            @RequestParam(defaultValue = "DEV")
            @Pattern(regexp = "DEV|PROD", message = "env must be DEV or PROD") String env
    ) {
        return service.diff(env);
    }

    @PostMapping("/run/{id}")
    public ScenarioRunResult run(
            @PathVariable("id") String id,
            @RequestParam(defaultValue = "DEV") String env
    ) {
        return service.run(id, env);
    }
}
