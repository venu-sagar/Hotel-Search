package com.hotel.search.entity;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;

/**
 * POJO for Cities Csv.
 */
@Entity
public class Cities {

    @CsvBindByName
    private long id;
    @CsvBindByName(column = "city_name", required = true)
    private String cityName;

    public Cities(long id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    public Cities() {    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
