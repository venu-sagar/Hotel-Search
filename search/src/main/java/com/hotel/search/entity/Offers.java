package com.hotel.search.entity;

/**
 * Custom datatype to hold the list of offers
 * in the search API.
 */
public class Offers {
    private long advertiserId;

    private String advertiseName;

    private int cpc;

    private int price;

    private String currency;

    public Offers(long advertiserId, String advertiseName, int cpc, int price, String currency) {
        this.advertiserId = advertiserId;
        this.advertiseName = advertiseName;
        this.cpc = cpc;
        this.price = price;
        this.currency = currency;
    }
    public Offers(){ }

    public long getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(long advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getAdvertiseName() {
        return advertiseName;
    }

    public void setAdvertiseName(String advertiseName) {
        this.advertiseName = advertiseName;
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
}
