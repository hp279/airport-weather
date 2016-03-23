package com.crossover.trial.weather.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected
 * values
 *
 * @author code test administrator
 */
public class DataPoint {

    public double mean = 0.0;

    public double first = 0;

    public double median = 0;

    public double third = 0;

    public int count = 0;

    private DataPoint(Builder builder) {
        this.setFirst(builder.first);
        this.setMean(builder.mean);
        this.setMedian(builder.median);
        this.setThird(builder.third);
        this.setCount(builder.count);
    }

    /**
     * the mean of the observations The mean value of the array.
     */
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * 1st quartile -- useful as a lower bound The first quartile is the 25th
     * percentile value in the array. That is, 25 percent of the array values
     * are smaller than this value and 75 percent are greater.
     */
    public double getFirst() {
        return first;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    /**
     * 2nd quartile -- median value The median value of the array.
     */
    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    /**
     * 3rd quartile value -- less noisy upper value The third quartile is the
     * 75th percentile value in the array. This means that 75 percent of the
     * array values are less than this value and 25 percent are greater.
     */
    public double getThird() {
        return third;
    }

    public void setThird(double third) {
        this.third = third;
    }

    /**
     * the total number of measurements The number of elements that participated
     * in the statistical calculations. For floating-point arrays, this does not
     * include any NaN or infinity (INF) values.
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object that) {
        return this.toString().equals(that.toString());
    }

    static public class Builder {
        private double mean = 0.0;
        private double first = 0;
        private double median = 0;
        private double third = 0;
        private int count = 0;

        public Builder() {
        }

        public Builder first(double first) {
            this.first = first;
            return this;
        }

        public Builder median(double median) {
            this.median = median;
            return this;
        }

        public Builder mean(double mean) {
            this.mean = mean;
            return this;
        }

        public Builder third(double third) {
            this.third = third;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}
