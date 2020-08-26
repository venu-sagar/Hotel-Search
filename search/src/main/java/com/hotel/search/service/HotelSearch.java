package com.hotel.search.service;

import com.hotel.search.entity.HotelAdvertiser;
import com.hotel.search.entity.ResultClass;

import java.util.List;
import java.util.Optional;

/**
 * Interface which tells us the main two methods search and price which has to be override by the controller.
 */
public interface HotelSearch {

    List<ResultClass> search(Optional<String> city, Optional<Long> startDate, Optional<Long> endDate);

    void price(List<HotelAdvertiser> newPrice) throws Exception;
}
