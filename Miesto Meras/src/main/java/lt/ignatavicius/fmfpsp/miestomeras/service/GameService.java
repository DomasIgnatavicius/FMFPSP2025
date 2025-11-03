package lt.ignatavicius.fmfpsp.miestomeras.service;

import lt.ignatavicius.fmfpsp.miestomeras.model.Rule;
import lt.ignatavicius.fmfpsp.miestomeras.model.State;

import java.util.List;

public interface GameService {

    public void reset();

    public State state();

    public List<Rule> rules();

    public State applyRule(int cityIndex, String ruleId);

    public State undo();

    public State redo();

    public State autoplay(int turns);
}
