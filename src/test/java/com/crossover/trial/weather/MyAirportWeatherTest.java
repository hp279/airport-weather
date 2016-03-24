package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.utils.AirportLoader;
import com.google.gson.Gson;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyAirportWeatherTest {

    private static final String BASE_URI = "http://localhost:9090";

    /** end point for read queries */
    private WebTarget queryWebTarget;

    /** end point to supply updates */
    private WebTarget collectWebTarget;

    private Gson _gson = new Gson();
    
    @Before
    public void setUp() throws Exception {
        Client client = ClientBuilder.newClient();
        queryWebTarget = client.target(BASE_URI).path("query");
        collectWebTarget = client.target(BASE_URI).path("collect");
    }

    @Test
    public void test1() throws Exception {
        String[] res = collectWebTarget.path("airports").request().accept(MediaType.APPLICATION_JSON)
                .get(String[].class);
        for (String iata : res) {
            collectWebTarget.path("airport").path(iata).request().delete();
        }

        res = collectWebTarget.path("airports").request().accept(MediaType.APPLICATION_JSON).get(String[].class);
        assertEquals(0, res.length);
    }

    @Test
    public void test2() throws Exception {
        BufferedReader br = null;
        try {
            URL url = AirportLoader.class.getClassLoader().getResource("airports_1000.dat");
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            AirportLoader al = new AirportLoader();
            al.upload(br);
        } finally {
            if (br != null) {
                br.close();
            }
        }

        String[] res = collectWebTarget.path("airports").request().accept(MediaType.APPLICATION_JSON)
                .get(String[].class);
        assertEquals(1000, res.length);
    }

    @Test
    public void test3() throws Exception {
        Response response = collectWebTarget.path("airport").path("4TW").request().accept(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
        
        response = collectWebTarget.path("airport").path("111").request().accept(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
    }
    
    @Test
    public void test4() throws Exception {
        WebTarget path = collectWebTarget.path("weather").path("4TW").path("wind");
        DataPoint dp = new DataPoint.Builder().count(10).first(10.0).median(20.0).third(30.0).mean(22.0).build();
        Response response = path.request().post(Entity.entity(dp, "application/json"));
        
        assertEquals(200, response.getStatus());
            
        path = collectWebTarget.path("weather").path("111").path("wind");
        response = path.request().post(Entity.entity(dp, "application/json"));
        
        assertEquals(422, response.getStatus());
    }
    
    @Test
    public void test5() throws Exception {
        WebTarget path = queryWebTarget.path("weather").path("4TW").path("0");
        Response response = path.request().get();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void test6() throws Exception {
        String[] res = collectWebTarget.path("airports").request().accept(MediaType.APPLICATION_JSON)
                .get(String[].class);
        for (String iata : res) {
            collectWebTarget.path("airport").path(iata).request().delete();
        }

        res = collectWebTarget.path("airports").request().accept(MediaType.APPLICATION_JSON).get(String[].class);
        assertEquals(0, res.length);
    }

}