package com.hotel.search.entity;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;

/**
 * POJO for Advertiser Csv.
 */
@Entity
public class Advertisers {

    @CsvBindByName
    private long id;
    @CsvBindByName(column = "advertiser_name", required = true)
    private String advertiserName;

    public Advertisers(long id, String advertiserName) {
        this.id = id;
        this.advertiserName = advertiserName;
    }

    public Advertisers(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }
}
