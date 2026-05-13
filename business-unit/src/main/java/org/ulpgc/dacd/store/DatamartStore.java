package org.ulpgc.dacd.store;

import org.ulpgc.dacd.model.Flight;
import org.ulpgc.dacd.model.SpaceWeather;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatamartStore {

    private final Map<String, Flight> activeFlights = new ConcurrentHashMap<>();
    private SpaceWeather latestSpaceWeather;
    private final List<Double> kpHistory = new CopyOnWriteArrayList<>();
    private final List<Double> polarDensityHistory = new CopyOnWriteArrayList<>();

    public void updateFlight(Flight flight) {
        activeFlights.put(flight.icao(), flight);
    }

    public void updateSpaceWeather(SpaceWeather spaceWeather) {
        this.latestSpaceWeather = spaceWeather;
        kpHistory.add(spaceWeather.kpIndex());
        recordPolarDensity();
    }

    private void recordPolarDensity() {
        long polarCount = activeFlights.values().stream()
                .filter(f -> Math.abs(f.latitude()) >= 60.0)
                .count();
        polarDensityHistory.add((double) polarCount);
    }

    public List<Double> getKpHistory() {
        return Collections.unmodifiableList(kpHistory);
    }

    public List<Double> getPolarDensityHistory() {
        return Collections.unmodifiableList(polarDensityHistory);
    }

    public Collection<Flight> getActiveFlights() {
        return activeFlights.values();
    }

    public SpaceWeather getLatestSpaceWeather() {
        return latestSpaceWeather;
    }
}