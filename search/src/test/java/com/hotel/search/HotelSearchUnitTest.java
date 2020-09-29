package com.hotel.search;

import com.hotel.search.service.HotelSearch;
import com.hotel.search.service.HotelService;
import com.hotel.search.web.HotelSearchController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test case for the application.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HotelSearchController.class)
public class HotelSearchUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    HotelService hotelService;

    @MockBean
    HotelSearch hotelSearch;

    /**
     * Search with required parameters
     *
     * @throws Exception
     */
    @Test
    public void hotelSearch() throws Exception {
        this.mockMvc.perform(get("/search")
                .param("city", "berlin")
                .param("startDate", "20200721")
                .param("endDate", "20201020"))
                .andExpect(status().isOk());
    }

    /**
     * test Search with bad requests.
     *
     * @throws Exception
     */
    @Test
    public void hotelSearchBadRequest() throws Exception {
        this.mockMvc.perform(get("/search")
                .param("city", "berlin")
                .param("startDate", "bad")
                .param("endDate", "request"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test search without the request params.
     *
     * @throws Exception
     */
    @Test
    public void hotelSearchWithoutParam() throws Exception {
        this.mockMvc.perform(get("/search"))
                .andExpect(status().isOk());
    }

    /**
     * Test method to post the price with required request body
     *
     * @throws Exception
     */
    @Test
    public void hotelSearchUpdatePrice() throws Exception {
        this.mockMvc.perform(post("/price").content("[{\"advertiserId\": 23,\n" +
                "\"hotelId\": 0,\n" +
                "\"cpc\": 500,\n" +
                "\"price\": 50000,\n" +
                "\"currency\": \"EUR\",\n" +
                "\"availability_start_date\": \"20200721\",\n" +
                "\"availability_end_date\": \"20201020\"\n" +
                "}]").contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }

    /**
     * Test method to post the price without request body
     *
     * @throws Exception
     */
    @Test
    public void hotelSearchUpdatePriceWithoutBody() throws Exception {
        this.mockMvc.perform(post("/price").content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    /**
     * Test method to post the price with empty request body
     *
     * @throws Exception
     */
    @Test
    public void hotelSearchUpdatePriceWithoutEmptyList() throws Exception {
        this.mockMvc.perform(post("/price").content("[]").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
