package com.crossover.trial.weather;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A reference implementation for the weather client. Consumers of the REST API can look at WeatherClient
 * to understand API semantics. This existing client populates the REST endpoint with dummy data useful for
 * testing.
 *
 * @author code test administrator
 */
public class WeatherClient {

    private static final String BASE_URI = "http://localhost:9090";

    /** end point for read queries */
    private WebTarget query;

    /** end point to supply updates */
    private WebTarget collect;

    public WeatherClient() {
        Client client = ClientBuilder.newClient();
        query = client.target(BASE_URI + "/query");
        collect = client.target(BASE_URI + "/collect");
    }

    public void pingCollect() {
        WebTarget path = collect.path("/ping");
        Response response = path.request().get();
        System.out.print("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    public void query(String iata) {
        WebTarget path = query.path("/weather/" + iata + "/0");
        Response response = path.request().get();
        System.out.println("query." + iata + ".0: " + response.readEntity(String.class));
    }

    public void pingQuery() {
        WebTarget path = query.path("/ping");
        Response response = path.request().get();
        System.out.println("query.ping: " + response.readEntity(String.class));
    }

    public void populate(String pointType, int first, int last, int mean, int median, int count) {
        WebTarget path = collect.path("/weather/BOS/" + pointType);
        DataPoint dp = new DataPoint.Builder()
                .withFirst(first).withLast(last).withMean(mean).withMedian(median).withCount(count)
                .build();
        Response post = path.request().post(Entity.entity(dp, "application/json"));
    }

    public void exit() {
        try {
            collect.path("/exit").request().get();
        } catch (Throwable t) {
            // swallow
        }
    }

    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient();
        wc.pingCollect();
        wc.populate("wind", 0, 10, 6, 4, 20);

        wc.query("BOS");
        wc.query("JFK");
        wc.query("EWR");
        wc.query("LGA");
        wc.query("MMU");

        wc.pingQuery();
        wc.exit();
        System.out.print("complete");
        System.exit(0);
    }
}
