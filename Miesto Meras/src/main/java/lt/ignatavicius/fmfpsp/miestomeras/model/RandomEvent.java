package lt.ignatavicius.fmfpsp.miestomeras.model;

public record RandomEvent(
        String title,
        String effectText,
        int dBudget,
        int dHap,
        int dSaf,
        int dEnv
) { }
