package com.crossover.trial.weather.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

    final Map<DataPointType, DataPoint> data = new ConcurrentHashMap<DataPointType, DataPoint>();

    /** the last time this data was updated, in milliseconds since UTC epoch */
    private long lastUpdateTime;

    public AtmosphericInformation() {
        lastUpdateTime = System.currentTimeMillis();
    }

    public DataPoint update(DataPointType key, DataPoint value) {
        lastUpdateTime = System.currentTimeMillis();
        return data.put(key, value);
    }

    public DataPoint get(DataPointType key) {
        return data.get(key);
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
}
