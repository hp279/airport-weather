package com.crossover.trial.weather.exception;

/**
 * An internal exception marker
 */
public class WeatherException extends Exception {
    private static final long serialVersionUID = -1648806779463348412L;

    public WeatherException(String message) {
        super(message);
    }

    public WeatherException(String message, Throwable throwable) {
        super(message, throwable);
    }
}