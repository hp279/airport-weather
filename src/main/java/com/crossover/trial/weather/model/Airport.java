package com.crossover.trial.weather.model;

import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 * 
 * @author code test administrator
 */
public class Airport {
    public static final String UNKNOWN = "Unknown";

    /** Main city served by airport. May be spelled differently from name. */
    private String city = UNKNOWN;

    /** Country or territory where airport is located. */
    private String country = UNKNOWN;

    /** 3-letter FAA code or IATA code */
    private String iata = UNKNOWN;

    /** 4-letter ICAO code (blank or "" if not assigned) */
    private String icao = UNKNOWN;

    /** latitude/longitude values in degrees */
    private Coordinate coordinate = null;

    /** Altitude In feet */
    private double altitude = 0.0;

    /**
     * Hours offset from UTC. Fractional hours are expressed as decimals. (e.g.
     * India is 5.5)
     */
    private TimeZone timeZone = TimeZone.getDefault();

    /**
     * One of E (Europe), A (US/Canada), S (South America), O (Australia), Z
     * (New Zealand), N (None) or U (Unknown)
     */
    private DaylightSavingsTime dst = DaylightSavingsTime.U;

    public Airport(String city, String country, String iata, String icao, double latitude, double longitude,
            double altitude, TimeZone timeZone, DaylightSavingsTime dst) {
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.icao = icao;
        this.coordinate = new Coordinate(latitude, longitude);
        this.altitude = altitude;
        this.timeZone = timeZone;
        this.dst = dst;
    }

    public Airport(String iata, double latitude, double longitude) {
        this.iata = iata;
        this.coordinate = new Coordinate(latitude, longitude);
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getAltitude() {
        return altitude;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public DaylightSavingsTime getDst() {
        return dst;
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

        Airport e = (Airport) other;

        return new EqualsBuilder().append(getIata(), e.getIata()).isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getIata());
    }
}
