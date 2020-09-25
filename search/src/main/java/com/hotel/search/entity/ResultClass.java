package com.hotel.search.entity;

import java.util.List;

/**
 * Custom Datatype for final result.
 */
public class ResultClass {
    private long hotel_id;
    private String hotel_name;
    private long rating;
    private int stars;
    private List<Offers> offers;

    public ResultClass(long hotel_id, String hotel_name, long rating, int stars, List<Offers> offers) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.rating = rating;
        this.stars = stars;
        this.offers = offers;
    }

    public ResultClass() { }

    public long getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(long hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
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

    public List<Offers> getOffers() {
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }
}
