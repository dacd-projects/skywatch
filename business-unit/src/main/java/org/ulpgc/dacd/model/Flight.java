package org.ulpgc.dacd.model;

public record Flight(
        String icao,
        String callsign,
        String country,
        double latitude,
        double longitude,
        double altitude,
        double velocity,
        String lastUpdate,
        long ts
) {}