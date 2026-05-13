package org.ulpgc.dacd.view;

import io.javalin.Javalin;
import org.ulpgc.dacd.control.AlertService;
import org.ulpgc.dacd.store.DatamartStore;
import org.ulpgc.dacd.control.CorrelationService;
import java.util.Map;

public class RestApi {

    private final DatamartStore datamartStore;
    private final AlertService alertService;
    private final CorrelationService correlationService;

    public RestApi(DatamartStore datamartStore, AlertService alertService, CorrelationService correlationService) {
        this.datamartStore = datamartStore;
        this.alertService = alertService;
        this.correlationService = correlationService;
    }

    private String interpretPearson(double r) {
        if (Double.isNaN(r)) return "Insufficient data";
        if (Math.abs(r) >= 0.7) return "Strong correlation";
        if (Math.abs(r) >= 0.4) return "Moderate correlation";
        return "Weak or no correlation";
    }

    public void start(int port) {
        Javalin app = Javalin.create().start(port);

        app.get("/flights", ctx -> ctx.json(datamartStore.getActiveFlights()));

        app.get("/weather", ctx -> ctx.json(datamartStore.getLatestSpaceWeather()));

        app.get("/alerts", ctx -> ctx.json(alertService.getFlightsAtRisk()));

        app.get("/correlation", ctx -> ctx.json(Map.of(
                "pearson", correlationService.getLastPearson(),
                "samples", correlationService.getSampleCount(),
                "interpretation", interpretPearson(correlationService.getLastPearson())
        )));

        System.out.println("REST API disponible en http://localhost:" + port);
    }
}
