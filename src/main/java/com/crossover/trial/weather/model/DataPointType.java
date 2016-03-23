package com.crossover.trial.weather.model;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType {
    WIND,
    /** temperature */
    TEMPERATURE,
    /** temperature in degrees celsius */
    HUMIDTY,
    /** humidity in percent */
    PRESSURE,
    /** pressure in mmHg */
    CLOUDCOVER,
    /** cloud cover percent from 0 - 100 (integer) */
    PRECIPITATION /** precipitation in cm */

}
