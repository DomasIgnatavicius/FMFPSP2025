package lt.ignatavicius.fmfpsp.miestomeras.service;

import lt.ignatavicius.fmfpsp.miestomeras.model.Rule;

import java.util.List;

public interface RuleService {

    public List<Rule> getRules();

    public Rule getById(String id);
}
