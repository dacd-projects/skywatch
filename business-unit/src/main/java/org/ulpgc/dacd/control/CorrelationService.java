package org.ulpgc.dacd.control;

import org.ulpgc.dacd.store.DatamartStore;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CorrelationService {

    private static final int MINIMUM_SAMPLES = 5;
    private final DatamartStore datamartStore;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private double lastPearson = Double.NaN;

    public CorrelationService(DatamartStore datamartStore) {
        this.datamartStore = datamartStore;
    }

    public void startPeriodicCalculation(int intervalMinutes) {
        scheduler.scheduleAtFixedRate(this::calculatePearson, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    private void calculatePearson() {
        List<Double> kpValues = datamartStore.getKpHistory();
        List<Double> polarValues = datamartStore.getPolarDensityHistory();

        int n = Math.min(kpValues.size(), polarValues.size());
        if (n < MINIMUM_SAMPLES) {
            lastPearson = Double.NaN;
            return;
        }

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        for (int i = 0; i < n; i++) {
            double x = kpValues.get(i);
            double y = polarValues.get(i);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        lastPearson = (denominator == 0) ? 0.0 : numerator / denominator;
    }

    public double getLastPearson() {
        return lastPearson;
    }

    public int getSampleCount() {
        return Math.min(datamartStore.getKpHistory().size(), datamartStore.getPolarDensityHistory().size());
    }
}
