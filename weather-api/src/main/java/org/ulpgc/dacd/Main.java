package org.ulpgc.dacd;

import org.ulpgc.dacd.control.ActiveMQSpaceWeatherPublisher;
import org.ulpgc.dacd.control.NasaFeeder;
import org.ulpgc.dacd.control.SpaceWeatherClient;
import org.ulpgc.dacd.control.SpaceWeatherController;
import org.ulpgc.dacd.control.SpaceWeatherStore;

public class Main {
    public static void main(String[] args) {

        NasaFeeder feeder = new SpaceWeatherClient();
        SpaceWeatherStore store = new ActiveMQSpaceWeatherPublisher();
        SpaceWeatherController controller = new SpaceWeatherController(feeder, store);
        controller.run();

    }
}