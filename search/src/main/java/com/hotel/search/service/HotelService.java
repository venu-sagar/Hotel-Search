package com.hotel.search.service;

import com.hotel.search.entity.Advertisers;
import com.hotel.search.entity.Cities;
import com.hotel.search.entity.HotelAdvertiser;
import com.hotel.search.entity.Hotels;

import java.util.List;
import java.util.Map;

/**
 * Interface which defines the helper functions for retrieving the respective data from csv files.
 */
public interface HotelService {
    Map<String, Cities> retrieveCities();
    Map<Long, Hotels> retrieveHotels();
    List<HotelAdvertiser> retrieveHotelAdvertisers();
    Map<Long, Advertisers> retrieveAdvertisers();
}
