package org.ulpgc.dacd;

import org.ulpgc.dacd.control.AlertService;
import org.ulpgc.dacd.control.DatamartUpdater;
import org.ulpgc.dacd.control.EventStoreReader;
import org.ulpgc.dacd.control.TopicSubscriber;
import org.ulpgc.dacd.view.RestApi;
import org.ulpgc.dacd.store.DatamartStore;
import org.ulpgc.dacd.control.CorrelationService;

public class Main {
    public static void main(String[] args) {
        String brokerUrl = args[0];
        String eventStorePath = args[1];
        String[] topics = {"Flight", "SpaceWeather"};

        DatamartStore datamartStore = new DatamartStore();
        DatamartUpdater datamartUpdater = new DatamartUpdater(datamartStore);

        EventStoreReader eventStoreReader = new EventStoreReader(eventStorePath, datamartUpdater);
        eventStoreReader.loadHistory();

        TopicSubscriber subscriber = new TopicSubscriber(brokerUrl, topics, datamartUpdater);
        subscriber.start();

        AlertService alertService = new AlertService(datamartStore);
        CorrelationService correlationService = new CorrelationService(datamartStore);
        correlationService.startPeriodicCalculation(5);

        RestApi restApi = new RestApi(datamartStore, alertService, correlationService);
        restApi.start(8080);
    }
}