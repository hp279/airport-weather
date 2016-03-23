package com.crossover.trial.weather.utils;

import com.crossover.trial.weather.model.Coordinate;

/**
 * Coordinate helper.
 *
 * @author hp279
 */
public class CoordinateHelper {
    /**
     * The minimum allowed latitude
     */
    public static double MIN_LATITUDE = -90.0;

    /**
     * The maximum allowed latitude
     */
    public static double MAX_LATITUDE = 90.0;

    /**
     * The minimum allowed longitude
     */
    public static double MIN_LONGITUDE = -180.0;

    /**
     * The maximum allowed longitude
     */
    public static double MAX_LONGITUDE = 180.0;

    /**
     * earth radius in KM
     */
    public static final double EARTH_RADIUS_KM = 6372.8;

    /**
     * A method to validate a latitude value
     *
     * @param latitude
     *            the latitude to check is valid
     *
     * @return true if, and only if, the latitude is within the MIN and MAX
     *         latitude
     */
    public static boolean isValidLatitude(double latitude) {
        return (latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE);
    }

    /**
     * A method to validate a longitude value
     *
     * @param longitude
     *            the longitude to check is valid
     *
     * @return true if, and only if, the longitude is between the MIN and MAX
     *         longitude
     */
    public static boolean isValidLongitude(double longitude) {
        return (longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param coord1
     *            coordinate 1
     * @param coord2
     *            coordinate 2
     * @return the distance in KM
     */
    public static double calculateDistance(Coordinate coord1, Coordinate coord2) {
        double deltaLat = Math.toRadians(coord2.getLatitude() - coord1.getLatitude());
        double deltaLon = Math.toRadians(coord2.getLongitude() - coord1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(coord1.getLatitude()) * Math.cos(coord2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }

}
