package org.ulpgc.dacd;

import org.ulpgc.dacd.control.ActiveMQFlightPublisher;
import org.ulpgc.dacd.control.FlightController;
import org.ulpgc.dacd.control.FlightFeeder;
import org.ulpgc.dacd.control.FlightStore;
import org.ulpgc.dacd.control.OpenSkyFlightFeeder;

public class Main {

    public static void main(String[] args) {
        FlightFeeder feeder = new OpenSkyFlightFeeder();
        FlightStore store = new ActiveMQFlightPublisher();
        FlightController controller = new FlightController(feeder, store);
        controller.run();

    }
}