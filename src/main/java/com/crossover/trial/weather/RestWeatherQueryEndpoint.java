package com.crossover.trial.weather;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPointType;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

import static com.crossover.trial.weather.RestWeatherCollectorEndpoint.addAirport;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    public final static Logger LOGGER = Logger.getLogger("WeatherQuery");

    /** earth radius in KM */
    public static final double R = 6372.8;

    /** shared gson json to object factory */
    public static final Gson gson = new Gson();

    /** all known airports */
    protected static List<Airport> airportData = new ArrayList<>();

    /**
     * atmospheric information for each airport, idx corresponds with
     * airportData
     */
    protected static List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */
    public static Map<Airport, Integer> requestFrequency = new HashMap<Airport, Integer>();

    public static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();

    static {
        init();
    }

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
        for (AtmosphericInformation ai : atmosphericInformation) {
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
        for (Airport data : airportData) {
            double frac = (double) requestFrequency.getOrDefault(data, 0) / requestFrequency.size();
            freq.put(data.getIata(), frac);
        }
        retval.put("iata_freq", freq);

        int m = radiusFreq.keySet().stream().max(Double::compare).orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
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
    public Response weather(String iata, String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            int idx = getAirportDataIdx(iata);
            retval.add(atmosphericInformation.get(idx));
        } else {
            Airport ad = findAirportData(iata);
            for (int i = 0; i < airportData.size(); i++) {
                if (calculateDistance(ad, airportData.get(i)) <= radius) {
                    AtmosphericInformation ai = atmosphericInformation.get(i);
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

    /**
     * Records information about how often requests are made
     *
     * @param iata
     *            an iata code
     * @param radius
     *            query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        Airport airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    public static Airport findAirportData(String iataCode) {
        return airportData.stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    public static int getAirportDataIdx(String iataCode) {
        Airport ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1
     *            airport 1
     * @param ad2
     *            airport 2
     * @return the distance in KM
     */
    public double calculateDistance(Airport ad1, Airport ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    /**
     * A dummy init method that loads hard coded data
     */
    protected static void init() {
        airportData.clear();
        atmosphericInformation.clear();
        requestFrequency.clear();

        addAirport("BOS", 42.364347, -71.005181);
        addAirport("EWR", 40.6925, -74.168667);
        addAirport("JFK", 40.639751, -73.778925);
        addAirport("LGA", 40.777245, -73.872608);
        addAirport("MMU", 40.79935, -74.4148747);
    }

}
