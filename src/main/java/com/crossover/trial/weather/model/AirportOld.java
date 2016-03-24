package com.crossover.trial.weather.model;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportOld {

    /** the three letter IATA code */
    private final String iata;

    /** latitude/longitude values in degrees */
    private final Coordinate coordinate;

    public AirportOld(String iata, double latitude, double longitude) {
        this.iata = iata;
        this.coordinate = new Coordinate(latitude, longitude);
    }

    public String getIata() {
        return iata;
    }

    public Coordinate getCoordinate() {
        return coordinate;
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

        AirportOld e = (AirportOld) other;

        return new EqualsBuilder().append(getIata(), e.getIata()).isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.iata);
    }
}
