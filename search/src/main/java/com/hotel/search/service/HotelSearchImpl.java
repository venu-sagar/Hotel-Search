package com.hotel.search.service;

import com.hotel.search.constants.HotelSearchConstants;
import com.hotel.search.entity.*;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main business logic for Hotel search {@link HotelSearch} and Hotel Service {@link HotelService}.
 * This class contains implementation of hotel search and price method.
 */
@Service
public class HotelSearchImpl implements HotelSearch, HotelService {

    private static Map<String, Cities> citiesList = new TreeMap<>();
    private static Map<Long, Hotels> hotelsList = new TreeMap<>();
    private static Map<Long, Advertisers> advertisersList = new TreeMap<>();
    private static List<HotelAdvertiser> hotelAdvertisersList = new ArrayList<>();

    /**
     * Retrives the hotels based on the params given.
     * Params are optional, so if any params are missed still it returns the list of hotels.
     * if all the params are missed, the query returns all the hotel search. (Takes a long time for the result to be visible in the rest call.)
     *
     * @param city      city name for the searching.
     * @param startDate hotel search from a certain date.
     * @param endDate   hotel search to a certain date.
     * @return final list of all the result for the rest call comprising the advertisers offers in a list for a hotel id.
     * @throws IOException to handle exception while retrieving data from csv file.
     */
    @Override
    public List<ResultClass> search(Optional<String> city, Optional<Long> startDate, Optional<Long> endDate) {

        //Retrieving the cities list from csv provided for the first time.
        if (this.citiesList.isEmpty()) {
            this.citiesList = retrieveCities();
        }

        // Retrieves the list of the hotels from the csv provided for the first time.
        if (this.hotelsList.isEmpty()) {
            this.hotelsList = retrieveHotels();
        }

        //Initializing the map with all the hotels list.
        Map<Long, Hotels> hotelsListByCityId = this.hotelsList;

        // Filtering the hotels list based on the city Id.
        if (city.isPresent()) {
            // retrieve Id of the city name provided in the query
            Long cityId;
            if (this.citiesList.containsKey(city.get().toLowerCase())) {
                cityId = this.citiesList.get(city.get().toLowerCase()).getId();
            } else {
                throw new EntityNotFoundException("City " + city.toString() + " not found in the database");
            }
            //Filter the hotels based on the city id.
            hotelsListByCityId.entrySet().removeIf(Id -> Id.getValue().getCityId() != cityId);
        }

        // collecting the Id's of the filtered hotels list.
        // Will be useful to filtering out all the hotels from the hotel advertiser list.
        List<Long> hotelIdListByCityId = hotelsListByCityId.keySet().stream().collect(Collectors.toList());

        // Retrieves the list of the hotel advertisers from the csv provided for the first time.
        if (this.hotelAdvertisersList.isEmpty()) {
            this.hotelAdvertisersList = retrieveHotelAdvertisers();
        }

        // Task -> To retrieve the list of hoteladvertisers based on cityId, startDate and endDate
        //Initialize the hotel advertisers variable with all the hotels filtered by the cityID.
        List<HotelAdvertiser> hotelAdvertisersListQueried = this.hotelAdvertisersList.stream()
                .filter(c -> hotelIdListByCityId.contains(c.getHotelId()))
                .collect(Collectors.toList());

        // Corner case to pass when the params(Start Date and End Date) are not provided.
        // Filter the hotel advertiser list based on the start and end date.
        if (startDate.isPresent() && endDate.isPresent()) {
            hotelAdvertisersListQueried = hotelAdvertisersListQueried.stream() //
                    .filter(c -> (c.getAvailabilityStartDate() >= startDate.get() //
                            && c.getAvailabilityEndDate() <= endDate.get())) //
                    .collect(Collectors.toList()); //
        }

        // Retrieves the list of the advertisers from the csv provided for the first time.
        if (this.advertisersList.isEmpty()) {
            this.advertisersList = retrieveAdvertisers();
        }

        // Format the above result in the form of the structure we desire.
        // Certain elements like hotel name, advertiser name were need to be retrieved.
        List<ResultClass> resultList = retrieveResult(hotelAdvertisersListQueried, this.advertisersList, hotelsListByCityId); //

        //Sorting the result list based on rating in descending order.
        resultList.sort(Comparator.comparingLong(ResultClass::getRating).reversed());

        return resultList;
    }

    /**
     * returns Final result in the form of map, later converted to list. Map is used for faster search.
     *
     * @param hotelAdvertisersListQueried list of all hotels retrieved based on city, startDate, endDate.
     * @param advertisersList             List of all advertisers to retrieve the advertiser name based on advertiser id.
     * @param hotelsListByCityId          List of all hotels filters by City ID to retrieve the hotel name based on advertiser id.
     * @return map containing, key as hotel id and the values as result class. Later the values are taken into a separate list which is what required for the search call.
     */
    private List<ResultClass> retrieveResult(List<HotelAdvertiser> hotelAdvertisersListQueried, Map<Long, Advertisers> advertisersList, Map<Long, Hotels> hotelsListByCityId) {
        Map<Long, ResultClass> resultList = new TreeMap<>();
        List<Offers> offersList = new ArrayList<>();
        Long rating = null;
        Integer stars = null;
        String hotelName = "", advertiserName = "";

        // For every hotel_advertiser filtered, retrieve hotel name, advertiser name, different offers.
        for (HotelAdvertiser hotelAdvertiser : hotelAdvertisersListQueried) {

            // retrieve the advertiser name
            advertiserName = advertisersList.get(hotelAdvertiser.getAdvertiserId()).getAdvertiserName();

            // for every hotel advertiser retrieve hotel name, advertiser name to store in result class.
            if (!resultList.isEmpty()) {
                if (resultList.containsKey(hotelAdvertiser.getHotelId())) {
                    // Add the previous offers from the advertisers for the same hotel_id.
                    offersList = resultList.get(hotelAdvertiser.getHotelId()).getOffers();
                }
            }
            //Enters only when there is a new hotel_id
            if (offersList.isEmpty()) {
                rating = hotelsListByCityId.get(hotelAdvertiser.getHotelId()).getRating();
                stars = hotelsListByCityId.get(hotelAdvertiser.getHotelId()).getStars();
                hotelName = hotelsListByCityId.get(hotelAdvertiser.getHotelId()).getName();
            }
            // Add the new offer from the advertisers
            offersList.add(new Offers(hotelAdvertiser.getAdvertiserId(), advertiserName, hotelAdvertiser.getCpc(), hotelAdvertiser.getPrice(), hotelAdvertiser.getCurrency()));

            // Sort the list intially by cpc in descending order and then sort the price in ascending order.
            offersList.sort(Comparator.comparingInt(Offers::getCpc).reversed().thenComparingInt(Offers::getPrice));

            // Add to the final result.
            resultList.put(hotelAdvertiser.getHotelId(), new ResultClass(hotelAdvertiser.getHotelId(), hotelName, rating, stars, new ArrayList<>(offersList)));
            offersList.clear();
        }

        return resultList.values().stream().collect(Collectors.toList());
    }

    /**
     * Updates the prices in the source list and store it.
     *
     * @param updatedPrice List of the Updated prices
     */
    @Override
    public void price(List<HotelAdvertiser> updatedPrice) throws Exception {

        // Retrieve the hotel advertisers list from the csv if it is not yet done.
        // this happens only when the update price happens before calling the search.
        if (this.hotelAdvertisersList.isEmpty()) {
            this.hotelAdvertisersList = retrieveHotelAdvertisers();
        }

        // Checks every element from the updated list with every element in the previous list
        // updates the prices in the original list.

        if (updatedPrice.isEmpty()) {
            throw new IllegalStateException("The list is Empty");
        }
        for (HotelAdvertiser hotelAdvertiser : updatedPrice) {
            this.hotelAdvertisersList.forEach(entry -> {
                if (hotelAdvertiser.getHotelId() == entry.getHotelId()
                        && hotelAdvertiser.getAdvertiserId() == entry.getAdvertiserId()) {
                    entry.setPrice(hotelAdvertiser.getPrice());
                }
            });
        }

    }


    /**
     * retrieves the list of cities from the cities csv file.
     *
     * @return Map containing key as a city name and value as city object.
     * @throws IOException to handle exception while reading from csv source.
     */
    @Override
    public Map<String, Cities> retrieveCities() {
        Map<String, Cities> cities = new TreeMap<>();
        InputStream in = getClass().getResourceAsStream(HotelSearchConstants.citiesPath);
        try {
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            CsvToBean<Cities> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Cities.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Reads all CSV contents into memory (Not suitable for large CSV files)
            csvToBean.parse().forEach(cityEntry -> {
                cities.put(cityEntry.getCityName().toLowerCase(), cityEntry);
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return cities;
    }

    /**
     * retrieves the list of Hotels from the hotels csv file.
     *
     * @return Map containing key as a hotel id and value as hotels object.
     * @throws IOException to handle exception while reading from csv source.
     */
    @Override
    public Map<Long, Hotels> retrieveHotels() {
        Map<Long, Hotels> hotelsList = new TreeMap<>();
        InputStream in = getClass().getResourceAsStream(HotelSearchConstants.hotelsPath);
        try {
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            CsvToBean<Hotels> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Hotels.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Reads all CSV contents into memory (Not suitable for large CSV files)
            csvToBean.parse().forEach(hotelEntry -> {
                hotelsList.put(hotelEntry.getId(), hotelEntry);
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return hotelsList;
    }

    /**
     * retrieves the list of Hotel_advertiser from the hotel_advertiser csv file.
     *
     * @return List containing HotelAdvertiser object.
     * @throws IOException to handle exception while reading from csv source.
     */
    @Override
    public List<HotelAdvertiser> retrieveHotelAdvertisers() {
        List<HotelAdvertiser> hotelAdvertisersList = new ArrayList<>();
        InputStream in = getClass().getResourceAsStream(HotelSearchConstants.hotelAdvertisersPath);
        try {
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            CsvToBean<HotelAdvertiser> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(HotelAdvertiser.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Reads all CSV contents into memory (Not suitable for large CSV files)
            hotelAdvertisersList = csvToBean.parse();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return hotelAdvertisersList;
    }

    /**
     * retrieves the list of Advertisers from the Advertisers csv file.
     *
     * @return Map containing key as a Advertiser id and value as Advertiser object.
     * @throws IOException to handle exception while reading from csv source.
     */
    @Override
    public Map<Long, Advertisers> retrieveAdvertisers() {
        Map<Long, Advertisers> advertisersList = new TreeMap<>();
        InputStream in = getClass().getResourceAsStream(HotelSearchConstants.advertisersPath);
        try {
            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            CsvToBean<Advertisers> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Advertisers.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Reads all CSV contents into memory (Not suitable for large CSV files)
            csvToBean.parse().forEach(advertiserEntry -> {
                advertisersList.put(advertiserEntry.getId(), advertiserEntry);
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return advertisersList;
    }

}
