package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Flight;

public class ActiveMQFlightPublisher implements FlightStore {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "Flight";
    private static final String SOURCE_ID = "flight-api";

    private final Gson gson = new Gson();
    private Session session;
    private MessageProducer producer;

    public ActiveMQFlightPublisher() {
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
    public void save(Flight flight) {
        try {
            JsonObject event = new JsonObject();
            event.addProperty("icao", flight.getIcao());
            event.addProperty("callsign", flight.getCallsign());
            event.addProperty("country", flight.getCountry());
            event.addProperty("latitude", flight.getLatitude());
            event.addProperty("longitude", flight.getLongitude());
            event.addProperty("altitude", flight.getAltitude());
            event.addProperty("velocity", flight.getVelocity());
            event.addProperty("lastUpdate", flight.getLastUpdate().toString());
            event.addProperty("ts", flight.getCapturedAt());
            event.addProperty("ss", SOURCE_ID);
            TextMessage message = session.createTextMessage(event.toString());
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}