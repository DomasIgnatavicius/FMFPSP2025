package lt.ignatavicius.fmfpsp.miestomeras.service.impl;

import lt.ignatavicius.fmfpsp.miestomeras.model.RandomEvent;
import lt.ignatavicius.fmfpsp.miestomeras.service.RandomEventProvider;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomEventProviderImpl implements RandomEventProvider {
    private final Random rnd = new Random();

    @Override
    public RandomEvent next() {
        int r = rnd.nextInt(100);
        if (r < 15) return new RandomEvent("Gaisras", "Gaisras nuniokojo rajoną", -20, -5, -10, -5);
        if (r < 25) return new RandomEvent("Protestas", "Gyventojai protestuoja prieš mokesčius", 0, -8, -3, 0);
        if (r < 32) return new RandomEvent("Ekologinis projektas", "Savivaldybė pasodino medžių", -5, +2, 0, +6);
        return null;
    }
}

