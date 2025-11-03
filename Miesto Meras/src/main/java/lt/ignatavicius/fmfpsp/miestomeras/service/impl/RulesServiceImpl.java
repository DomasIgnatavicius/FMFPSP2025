package lt.ignatavicius.fmfpsp.miestomeras.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.ignatavicius.fmfpsp.miestomeras.model.Rule;
import lt.ignatavicius.fmfpsp.miestomeras.service.RuleService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RulesServiceImpl implements RuleService {
    private final List<Rule> rules;

    public RulesServiceImpl(ObjectMapper mapper) throws IOException {
        var res = new ClassPathResource("rules.json");
        this.rules = mapper.readValue(res.getInputStream(), new TypeReference<List<Rule>>() {
        });
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Rule getById(String id) {
        return rules.stream().filter(r -> r.id().equals(id)).findFirst().orElse(null);
    }
}

