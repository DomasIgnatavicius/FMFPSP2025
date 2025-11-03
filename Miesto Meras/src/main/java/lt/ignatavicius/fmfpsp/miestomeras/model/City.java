package lt.ignatavicius.fmfpsp.miestomeras.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class City {
    private final String name;
    private final int population;
    private final int budget;
    private final Indicators indicators;

    public City(String name, int population, int budget, Indicators indicators) {
        this.name = Objects.requireNonNull(name);
        this.population = Math.max(0, population);
        this.budget = Math.max(0, budget);
        this.indicators = indicators == null ? new Indicators(60,60,60) : indicators.clamp();
    }
    public City withBudget(int newBudget) {
        return new City(name, population, Math.max(0, newBudget), indicators);
    }
    public City withIndicators(Indicators ind) {
        return new City(name, population, budget, ind == null ? indicators : ind.clamp());
    }
    public boolean isBankrupt() {
        return budget <= 0;
    }
}
