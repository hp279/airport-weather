package com.crossover.trial.weather.model;

/**
 * Daylight savings time.
 *
 * @author hp279
 */
public enum DaylightSavingsTime {
    E("Europe"), A("US/Canada"), S("South America"), O("Australia"), Z("New Zealand"), N("None"), U("Unknown");

    private final String title;

    private DaylightSavingsTime(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
}
