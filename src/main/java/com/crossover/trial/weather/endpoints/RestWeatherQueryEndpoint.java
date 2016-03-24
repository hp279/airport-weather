package com.crossover.trial.weather.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPointType;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.crossover.trial.weather.utils.CoordinateHelper;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQuery {
    public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

    private AirportWeatherService awService = AirportWeatherService.INSTANCE;

    /** shared gson json to object factory */
    public static final Gson gson = new Gson();

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();

        int datasize = 0;
        for (AtmosphericInformation ai : awService.getAirportData().values()) {
            // we only count recent readings
            if (ai.get(DataPointType.CLOUDCOVER) != null || ai.get(DataPointType.HUMIDTY) != null
                    || ai.get(DataPointType.PRECIPITATION) != null || ai.get(DataPointType.PRESSURE) != null
                    || ai.get(DataPointType.TEMPERATURE) != null || ai.get(DataPointType.WIND) != null) {
                // updated in the last day
                if (ai.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                    datasize++;
                }
            }
        }
        retval.put("datasize", datasize);

        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (Airport airport : awService.getAirports()) {
            double frac = (double) awService.getRequestFrequency().getOrDefault(airport, 0)
                    / awService.getRequestFrequency().size();
            freq.put(airport.getIata(), frac);
        }
        retval.put("iata_freq", freq);

        final int m = awService.getRadiusFreq().keySet().stream().max(Double::compare).orElse(1000.0).intValue() + 1;

        final int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : awService.getRadiusFreq().entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        retval.put("radius_freq", hist);

        return gson.toJson(retval);
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the
     * requested airport information and return a list of matching atmosphere
     * information.
     *
     * @param iata
     *            the iataCode
     * @param radiusString
     *            the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(@PathParam("iata") String iata, @PathParam("radius") String radiusString) {

        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        awService.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            retval.add(awService.getAtmosphericInformation(iata));
        } else {
            Airport airport = awService.findAirport(iata);

            for (Airport airport2 : awService.getAirports()) {
                if (CoordinateHelper.calculateDistance(airport.getCoordinate(), airport2.getCoordinate()) <= radius) {

                    AtmosphericInformation ai = awService.getAirportData().get(airport2);

                    if (ai.get(DataPointType.CLOUDCOVER) != null || ai.get(DataPointType.HUMIDTY) != null
                            || ai.get(DataPointType.PRECIPITATION) != null || ai.get(DataPointType.PRESSURE) != null
                            || ai.get(DataPointType.TEMPERATURE) != null || ai.get(DataPointType.WIND) != null) {
                        retval.add(ai);
                    }
                }
            }

        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }
}
