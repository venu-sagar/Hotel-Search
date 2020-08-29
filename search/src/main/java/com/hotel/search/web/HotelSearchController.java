package com.hotel.search.web;

import com.hotel.search.entity.HotelAdvertiser;
import com.hotel.search.entity.ResultClass;
import com.hotel.search.service.HotelSearch;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Controller which gives us the end points for hotel search and update the price.
 */
@RestController
public class HotelSearchController {

    @Autowired
    public HotelSearch hotelSearch;

    @GetMapping("/search")
    @ApiOperation(value = "Searches hotels",
            notes = "This API return the hotel based on the parameters city, start and the end dates")
    public ResponseEntity<List<ResultClass>> hotelSearch(
            @ApiParam(name = "city",
                    type = "Optional<String>",
                    value = "Name of the city",
                    example = "Berlin",
                    required = false)
            @RequestParam Optional<String> city,
            @ApiParam(name = "startDate",
                    type = "Optional<Long>",
                    value = "Start date of the hotel search",
                    example = "20200721",
                    required = false)
            @RequestParam Optional<Long> startDate,
            @ApiParam(name = "endDate",
                    type = "Optional<Long>",
                    value = "End date of the hotel search",
                    example = "20201020",
                    required = false)
            @RequestParam Optional<Long> endDate) throws Exception {
        List<ResultClass> list;
        try {
            list = hotelSearch.search(city, startDate, endDate);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<ResultClass>>(list, HttpStatus.OK);
    }

    @PostMapping("/price")
    @ApiOperation(value = "Updates the price",
            notes = "This API updates the price of the hotel present in the request body")
    public ResponseEntity updatePrice(
            @ApiParam(name = "Body for the update of the price",
                    type = "list of the hotel advertisers",
                    value = "",
                    example = "[{\"advertiserId\": 23,\n" +
                            "\"hotelId\": 0,\n" +
                            "\"cpc\": 500,\n" +
                            "\"price\": 50000,\n" +
                            "\"currency\": \"EUR\",\n" +
                            "\"availability_start_date\": \"20200721\",\n" +
                            "\"availability_end_date\": \"20201020\"\n" +
                            "}]",
                    required = true)
            @RequestBody List<HotelAdvertiser> newPrice) throws Exception {
        try {
            hotelSearch.price(newPrice);
        } catch (IllegalStateException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
