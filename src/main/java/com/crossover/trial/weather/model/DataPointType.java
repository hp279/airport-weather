package com.crossover.trial.weather.model;

import com.crossover.trial.weather.exception.WeatherException;

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
    PRECIPITATION;
    /** precipitation in cm 
     * @throws WeatherException */

    public void validate(DataPoint dataPoint) throws WeatherException {
        boolean valid = true;

        switch (this) {
        case WIND:
            valid = dataPoint.getMean() >= 0;    
            break;
        case TEMPERATURE:
            valid = dataPoint.getMean() >= -50 && dataPoint.getMean() < 100;
            break;
        case HUMIDTY:
            valid = dataPoint.getMean() >= 0 && dataPoint.getMean() < 100;
            break;
        case PRESSURE:
            valid = dataPoint.getMean() >= 650 && dataPoint.getMean() < 800;
            break;
        case CLOUDCOVER:
            valid = dataPoint.getMean() >= 0 && dataPoint.getMean() < 100;
            break;
        case PRECIPITATION:
            valid = dataPoint.getMean() >= 0 && dataPoint.getMean() < 100;
            break;
        default:
            break;
        }

        if (!valid) {
            throw new WeatherException(this, "Invalid atmospheric data parameter");
        }
    }
}
