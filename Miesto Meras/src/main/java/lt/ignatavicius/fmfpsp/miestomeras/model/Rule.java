package lt.ignatavicius.fmfpsp.miestomeras.model;

public record Rule(
        String id,
        String name,
        String description,
        int dBudget,
        int dHappiness,
        int dSafety,
        int dEnvironment
) {}
