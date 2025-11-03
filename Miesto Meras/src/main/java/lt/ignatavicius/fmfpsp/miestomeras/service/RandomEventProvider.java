package lt.ignatavicius.fmfpsp.miestomeras.service;

import lt.ignatavicius.fmfpsp.miestomeras.model.RandomEvent;

public interface RandomEventProvider {
    RandomEvent next();
}
