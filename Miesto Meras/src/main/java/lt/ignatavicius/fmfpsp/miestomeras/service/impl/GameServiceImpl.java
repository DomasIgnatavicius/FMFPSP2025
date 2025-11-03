package lt.ignatavicius.fmfpsp.miestomeras.service.impl;

import lt.ignatavicius.fmfpsp.miestomeras.config.game.GameConfig;
import lt.ignatavicius.fmfpsp.miestomeras.model.*;
import lt.ignatavicius.fmfpsp.miestomeras.service.GameService;
import lt.ignatavicius.fmfpsp.miestomeras.service.RandomEventProvider;
import lt.ignatavicius.fmfpsp.miestomeras.service.RuleService;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private final RuleService rulesService;
    private final RandomEventProvider events;
    private final GameConfig cfg = GameConfig.defaults();

    private State currentState;
    private final Deque<State> undo = new ArrayDeque<>();
    private final Deque<State> redo = new ArrayDeque<>();

    public GameServiceImpl(RulesServiceImpl rulesService, RandomEventProvider events) {
        this.rulesService = rulesService;
        this.events = events;
        reset();
    }

    public synchronized void reset() {
        this.currentState = new State(List.of(
                new City("Vilnius", 500_000, 50, new Indicators(60, 60, 60))
        ), 1, State.Outcome.IN_PROGRESS.name(), new ArrayList<>());
        undo.clear();
        redo.clear();
    }

    public synchronized State state() {
        return currentState;
    }

    public synchronized List<Rule> rules() {
        return rulesService.getRules();
    }

    public synchronized State applyRule(int cityIndex, String ruleId) {
        if (!State.Outcome.IN_PROGRESS.name().equals(currentState.outcome())) return currentState;
        Rule rule = rulesService.getById(ruleId);
        if (rule == null) return currentState;

        pushUndo();

        List<City> list = new ArrayList<>(currentState.cities());
        City before = list.get(cityIndex);
        City after = before.withBudget(before.getBudget() + rule.dBudget());
        after = after.withIndicators(after.getIndicators().plus(rule.dHappiness(), rule.dSafety(), rule.dEnvironment()));
        list.set(cityIndex, after);

        var log = new ArrayList<>(currentState.log());
        log.add("Turas " + currentState.turn() + " | " + before.getName() + ": " + rule.name() +
                " (ΔB:" + rule.dBudget() + ", ΔH:" + rule.dHappiness() + ", ΔS:" + rule.dSafety() + ", ΔE:" + rule.dEnvironment() + ")");

        RandomEvent ev = events.next();
        if (ev != null) {
            after = after.withBudget(after.getBudget() + ev.dBudget());
            after = after.withIndicators(after.getIndicators().plus(ev.dHap(), ev.dSaf(), ev.dEnv()));
            list.set(cityIndex, after);
            log.add("  Atsitiktinis įvykis — " + ev.title() + ": " + ev.effectText());
        }

        String outcome = evaluate(after);
        int nextTurn = State.Outcome.IN_PROGRESS.name().equals(outcome) ? currentState.turn() + 1 : currentState.turn();

        if (nextTurn > cfg.maxTurns()){
            outcome = State.Outcome.WON.name();
        }

        currentState = new State(List.copyOf(list), nextTurn, outcome, List.copyOf(log));
        redo.clear();
        return currentState;
    }

    private String evaluate(City c) {
        if (c.isBankrupt()){
            return State.Outcome.LOST_BUDGET.name();
        }
        if (c.getIndicators().happiness() <= cfg.minHappinessToLose()){
            return State.Outcome.LOST_HAPPINESS.name();
        }
        return State.Outcome.IN_PROGRESS.name();
    }

    public synchronized State undo() {
        if (undo.isEmpty()){
            return currentState;
        }
        redo.push(currentState);
        currentState = undo.pop();
        return currentState;
    }

    public synchronized State redo() {
        if (redo.isEmpty()) return currentState;
        undo.push(currentState);
        currentState = redo.pop();
        return currentState;
    }

    private void pushUndo() {
        undo.push(cloneState(currentState));
    }

    private State cloneState(State s) {
        List<City> cities = new ArrayList<>();
        for (City c : s.cities()) {
            cities.add(new City(c.getName(), c.getPopulation(), c.getBudget(),
                    new Indicators(c.getIndicators().happiness(), c.getIndicators().safety(), c.getIndicators().environment())));
        }
        return new State(List.copyOf(cities), s.turn(), s.outcome(), new ArrayList<>(s.log()));
    }

    public synchronized State autoplay(int turns) {
        for (int i = 0; i < turns; i++) {
            if (!State.Outcome.IN_PROGRESS.name().equals(currentState.outcome())) break;
            int cityIdx = i % currentState.cities().size();
            Rule pick = autoPickRule();
            applyRule(cityIdx, pick.id());
        }
        return currentState;
    }

    private Rule autoPickRule() {
        List<Rule> rules = rulesService.getRules();
        City focus = currentState.cities().get(0);
        boolean boostHap = focus.getIndicators().happiness() < 50;
        Rule best = rules.get(0);
        for (Rule r : rules) {
            if (boostHap) {
                if (r.dHappiness() > best.dHappiness()) best = r;
            } else {
                if (r.dBudget() > best.dBudget()) best = r;
            }
        }
        return best;
    }
}
