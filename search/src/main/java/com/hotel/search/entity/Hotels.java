package com.hotel.search.entity;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;

/**
 * POJO for Hotels Csv.
 */
@Entity
public class Hotels {

    @CsvBindByName
    private long id;
    @CsvBindByName(column = "city_id", required = true)
    private long cityId;
    @CsvBindByName
    private long clicks;
    @CsvBindByName
    private long impressions;
    @CsvBindByName
    private String name;
    @CsvBindByName
    private long rating;
    @CsvBindByName
    private int stars;

    public Hotels(long id, long cityId, long clicks, long impressions, String name, long rating, int stars) {
        this.id = id;
        this.cityId = cityId;
        this.clicks = clicks;
        this.impressions = impressions;
        this.name = name;
        this.rating = rating;
        this.stars = stars;
    }

    public Hotels() {  }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
