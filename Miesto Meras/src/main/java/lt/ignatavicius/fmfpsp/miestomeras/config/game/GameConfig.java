package lt.ignatavicius.fmfpsp.miestomeras.config.game;

public record GameConfig(int maxTurns, int minHappinessToLose) {
    public static GameConfig defaults() {
        return new GameConfig(12, 10);
    }
}
