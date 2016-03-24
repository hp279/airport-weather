package com.crossover.trial.weather.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.crossover.trial.weather.exception.WeatherException;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.model.DataPointType;

/**
 * 
 * @author hp279
 *
 */
public enum AirportWeatherService {
    INSTANCE;

    /**
     * atmospheric information for each airport, keys - all known airports
     */
    private Map<Airport, AtmosphericInformation> airportData = new HashMap<>();

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */
    private Map<Airport, Integer> requestFrequency = new HashMap<Airport, Integer>();

    private Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();

    public Set<Airport> getAirports() {
        return airportData.keySet();
    }

    public Map<Airport, AtmosphericInformation> getAirportData() {
        return airportData;
    }

    public Map<Airport, Integer> getRequestFrequency() {
        return requestFrequency;
    }

    public Map<Double, Integer> getRadiusFreq() {
        return radiusFreq;
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
        Airport airport = findAirport(iata);
        getRequestFrequency().put(airport, getRequestFrequency().getOrDefault(airport, 0) + 1);
        getRadiusFreq().put(radius, getRadiusFreq().getOrDefault(radius, 0));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    public Airport findAirport(String iataCode) {
        return getAirports().stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    public AtmosphericInformation getAtmosphericInformation(String iataCode) throws NoSuchElementException {
        final Airport airport = findAirport(iataCode);
        if (airport == null) {
            throw new NoSuchElementException();
        } 
        return getAirportData().get(airport);
    }

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode
     *            the 3 letter IATA code
     * @param pointType
     *            the point type {@link DataPointType}
     * @param dp
     *            a datapoint object holding pointType data
     *
     * @throws WeatherException
     *             if the update can not be completed NoSuchElementException if
     *             airport founded
     */
    public void addDataPoint(String iataCode, String pointType, DataPoint dp)
            throws NoSuchElementException, WeatherException {

        final AtmosphericInformation ai = getAtmosphericInformation(iataCode);
        if (ai == null) {
            throw new NoSuchElementException();
        } else {
            updateAtmosphericInformation(ai, pointType, dp);
        }
    }

    /**
     * update atmospheric information with the given data point for the given
     * point type
     *
     * @param ai
     *            the atmospheric information object to update
     * @param pointType
     *            the data point type as a string
     * @param dp
     *            the actual data point
     * 
     * @throws WeatherException
     *             if the update can not be completed
     */
    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp)
            throws WeatherException {
        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());
        ai.update(dptype, dp);
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode
     *            3 letter code
     * @param latitude
     *            in degrees
     * @param longitude
     *            in degrees
     *
     * @return true if airport was added, false - if not (already exists)
     */
    public boolean addAirport(String iataCode, double latitude, double longitude) {
        Airport airport = new Airport(iataCode, latitude, longitude);
        boolean alreadyExist = false;
        alreadyExist = getAirportData().containsKey(airport);
        if (!alreadyExist) {
            getAirportData().put(airport, new AtmosphericInformation());
        }
        return alreadyExist;
    }

    /**
     * Remove airport from the list
     *
     * @param iataCode
     *            3 letter code
     * @param latitude
     *            in degrees
     * @param longitude
     *            in degrees
     *
     * @return true if airport was added, false - if not (already exists)
     */
    public boolean deleteAirport(String iataCode) {
        final Airport airport = findAirport(iataCode);
        if (airport == null) {
            return false;
        }
        return getAirportData().remove(airport) != null;
    }
}
