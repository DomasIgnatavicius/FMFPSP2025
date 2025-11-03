package lt.ignatavicius.fmfpsp.miestomeras.model;

import java.util.List;

public record State(
        List<City> cities,
        int turn,
        String outcome,
        List<String> log) {
    public enum Outcome { IN_PROGRESS, WON, LOST_BUDGET, LOST_HAPPINESS }
}
