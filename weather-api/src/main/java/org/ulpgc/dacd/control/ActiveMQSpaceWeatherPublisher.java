package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.SpaceWeather;

public class ActiveMQSpaceWeatherPublisher implements SpaceWeatherStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "SpaceWeather";
    private static final String SOURCE_ID = "weather-api";

    private final Gson gson = new Gson();
    private Session session;
    private MessageProducer producer;

    public ActiveMQSpaceWeatherPublisher() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(SpaceWeather event) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("kpIndex", event.getKpIndex());
            json.addProperty("startTime", event.getStartTime().toString());
            json.addProperty("endTime", event.getEndTime().toString());
            json.addProperty("capturedAt", event.getCapturedAt());
            json.addProperty("ts", event.getCapturedAt());
            json.addProperty("ss", SOURCE_ID);

            TextMessage message = session.createTextMessage(json.toString());
            producer.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}