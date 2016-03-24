package com.crossover.trial.weather;

import com.crossover.trial.weather.endpoints.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.endpoints.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.endpoints.WeatherCollector;
import com.crossover.trial.weather.endpoints.WeatherQuery;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.model.DataPointType;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WeatherEndpointTest {

    private WeatherQuery _query = new RestWeatherQueryEndpoint();

    private WeatherCollector _update = new RestWeatherCollectorEndpoint();

    private Gson _gson = new Gson();

    private DataPoint _dp;

    @Before
    public void setUp() throws Exception {
        AirportWeatherService.INSTANCE.getAirportData().clear();
        AirportWeatherService.INSTANCE.getRequestFrequency().clear();

        AirportWeatherService.INSTANCE.addAirport("BOS", 42.364347, -71.005181);
        AirportWeatherService.INSTANCE.addAirport("EWR", 40.6925, -74.168667);
        AirportWeatherService.INSTANCE.addAirport("JFK", 40.639751, -73.778925);
        AirportWeatherService.INSTANCE.addAirport("LGA", 40.777245, -73.872608);
        AirportWeatherService.INSTANCE.addAirport("MMU", 40.79935, -74.4148747);

        _dp = new DataPoint.Builder().count(10).first(10.0).median(20.0).third(30.0).mean(22.0).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(_dp));
        _query.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
        assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).get(DataPointType.WIND), _dp);
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        _update.updateWeather("JFK", "wind", _gson.toJson(_dp));
        _dp.setMean(40);
        _update.updateWeather("EWR", "wind", _gson.toJson(_dp));
        _dp.setMean(30);
        _update.updateWeather("LGA", "wind", _gson.toJson(_dp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("JFK", "200").getEntity();
        assertEquals(3, ais.size());
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder().count(10).first(10.0).median(20.0).third(30.0).mean(22.0).build();
        _update.updateWeather("BOS", "wind", _gson.toJson(windDp));
        _query.weather("BOS", "0").getEntity();

        String ping = _query.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

        DataPoint cloudCoverDp = new DataPoint.Builder().count(4).first(10.0).median(60.0).third(100.0).mean(50.0)
                .build();
        _update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

        List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
        assertEquals(ais.get(0).get(DataPointType.WIND), windDp);
        assertEquals(ais.get(0).get(DataPointType.CLOUDCOVER), cloudCoverDp);
    }

}