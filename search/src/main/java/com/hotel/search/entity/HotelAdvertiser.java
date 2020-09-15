package com.hotel.search.entity;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;

/**
 * POJO for HotelAdvertiser Csv.
 */
@Entity
public class HotelAdvertiser {

    @CsvBindByName(column = "advertiser_id", required = true)
    private long advertiserId;
    @CsvBindByName(column = "hotel_id", required = true)
    private long hotelId;
    @CsvBindByName
    private int cpc;
    @CsvBindByName
    private int price;
    @CsvBindByName
    private String currency;
    @CsvBindByName(column = "availability_start_date", required = true)
    private long availabilityStartDate;
    @CsvBindByName(column = "availability_end_date", required = true)
    private long availabilityEndDate;

    public HotelAdvertiser(long advertiserId, long hotelId, int cpc, int price, String currency, long availabilityStartDate, long availabilityEndDate) {
        this.advertiserId = advertiserId;
        this.hotelId = hotelId;
        this.cpc = cpc;
        this.price = price;
        this.currency = currency;
        this.availabilityStartDate = availabilityStartDate;
        this.availabilityEndDate = availabilityEndDate;
    }

    public HotelAdvertiser() { }

    public long getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(long advertiserId) {
        this.advertiserId = advertiserId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public int getCpc() {
        return cpc;
    }

    public void setCpc(int cpc) {
        this.cpc = cpc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAvailabilityStartDate() {
        return availabilityStartDate;
    }

    public void setAvailabilityStartDate(long availabilityStartDate) {
        this.availabilityStartDate = availabilityStartDate;
    }

    public long getAvailabilityEndDate() {
        return availabilityEndDate;
    }

    public void setAvailabilityEndDate(long availabilityEndDate) {
        this.availabilityEndDate = availabilityEndDate;
    }
}
