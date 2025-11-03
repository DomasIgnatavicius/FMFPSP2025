package lt.ignatavicius.fmfpsp.miestomeras.model;

public record Indicators(int happiness, int safety, int environment) {
    public Indicators clamp() {
        return new Indicators(
            Math.max(0, Math.min(100, happiness)),
            Math.max(0, Math.min(100, safety)),
            Math.max(0, Math.min(100, environment))
        );
    }
    public Indicators plus(int dh, int ds, int de) {
        return new Indicators(happiness + dh, safety + ds, environment + de).clamp();
    }
}
