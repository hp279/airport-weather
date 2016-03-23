package com.crossover.trial.weather.model;

import com.crossover.trial.weather.utils.CoordinateHelper;

public class Coordinate {
    /** latitude value in degrees */
    private double latitude;

    /** longitude value in degrees */
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        if (CoordinateHelper.isValidLatitude(latitude)) {
            this.latitude = latitude;
        } else {
            throw new IllegalArgumentException("Invalid latitude");
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        if (CoordinateHelper.isValidLongitude(longitude)) {
            this.longitude = longitude;
        } else {
            throw new IllegalArgumentException("Invalid longitude");
        }
    }
}
