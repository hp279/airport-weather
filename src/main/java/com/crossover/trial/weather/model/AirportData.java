package com.crossover.trial.weather.model;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.crossover.trial.weather.utils.CoordinateHelper;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /** the three letter IATA code */
    private final String iata;

    /** latitude value in degrees */
    private final double latitude;

    /** longitude value in degrees */
    private final double longitude;

    public AirportData(String iata, double latitude, double longitude) {
        this.iata = iata;

        if (CoordinateHelper.isValidLatitude(latitude)) {
            this.latitude = latitude;
        } else {
            throw new IllegalArgumentException("Invalid latitude");
        }

        if (CoordinateHelper.isValidLongitude(longitude)) {
            this.longitude = longitude;
        } else {
            throw new IllegalArgumentException("Invalid longitude");
        }
    }

    public String getIata() {
        return iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (other.getClass() != getClass())
            return false;

        AirportData e = (AirportData) other;

        return new EqualsBuilder().append(getIata(), e.getIata()).isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.iata);
    }
}
