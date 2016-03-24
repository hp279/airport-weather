package com.crossover.trial.weather;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.service.AirportWeatherService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class WeatherCollectorEndpointImpl implements WeatherCollectorEndpoint {
    public final static Logger LOGGER = Logger.getLogger(WeatherCollectorEndpointImpl.class.getName());

    private AirportWeatherService awService = AirportWeatherService.INSTANCE;

    /** shared gson json to object factory */
    public final static Gson gson = new Gson();

    @Override
    @GET
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    @POST
    @Path("/weather/{iata}/{pointType}")
    public Response updateWeather(
            @PathParam("iata") String iataCode, 
            @PathParam("pointType") String pointType,
            String datapointJson) {
        try {
            awService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
            return Response.status(Response.Status.OK).build();
        } catch (JsonSyntaxException | WeatherException | NoSuchElementException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            return Response.status(422).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    @GET
    @Path("/airports")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirports() {
        final Set<String> retval = new HashSet<>();
        for (Airport airport : awService.getAirports()) {
            retval.add(airport.getIata());
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }

    @Override
    @GET
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirport(@PathParam("iata") String iata) {
        final Airport airport = awService.findAirport(iata);

        return Response.status(airport != null ? Response.Status.OK : Response.Status.NOT_FOUND)
                .entity(airport)
                .build();
    }

    @Override
    @POST
    @Path("/airport/{iata}/{lat}/{long}")
    public Response addAirport(
            @PathParam("iata") String iata, 
            @PathParam("lat") String latString,
            @PathParam("long") String longString) {
        try {
            awService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
            return Response.status(Response.Status.CREATED).build();
        } catch (NumberFormatException nfe){
            return Response.status(422).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    @DELETE
    @Path("/airport/{iata}")
    public Response deleteAirport(@PathParam("iata") String iata) {
        try {
            boolean deleted = awService.deleteAirport(iata);
            return Response.status(deleted ? Response.Status.OK : Response.Status.NOT_FOUND).build();
        } catch (NumberFormatException e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(422).build();
        }
    }
}
