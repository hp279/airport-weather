package com.crossover.trial.weather.utils;

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
     * A method to validate a latitude value
     *
     * @param latitude the latitude to check is valid
     *
     * @return true if, and only if, the latitude is within the MIN and MAX latitude
     */
    public static boolean isValidLatitude(double latitude) {
      return (latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE);
    }
    
    /**
     * A method to validate a longitude value
     *
     * @param longitude the longitude to check is valid
     *
     * @return true if, and only if, the longitude is between the MIN and MAX longitude
     */
    public static boolean isValidLongitude(double longitude) {
      return (longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE);
    }

}
