package com.crossover.trial.weather;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The query only API for the Weather Server App. This API is made available to the public internet.
 *
 * @author code test adminsitrator
 */
public interface WeatherQueryEndpoint {

    /**
     * Retrieve health and status information for the the query api. Returns information about how the number
     * of datapoints currently held in memory, the frequency of requests for each IATA code and the frequency of
     * requests for each radius.
     *
     * @return a JSON formatted dict with health information.
     */
    @GET
    @Path("/ping")
    String ping();

    /**
     * Retrieve the most up to date atmospheric information from the given airport and other airports in the given
     * radius.
     *
     * @param iata the three letter airport code
     * @param radiusString the radius, in km, from which to collect weather data
     *
     * @return an HTTP Response and a list of {@link AtmosphericInformation} from the requested airport and
     * airports in the given radius
     */
    @GET
    @Path("/weather/{iata}/{radius}")
    @Produces(MediaType.APPLICATION_JSON)
    Response weather(@PathParam("iata") String iata, @PathParam("radius") String radiusString);
}
