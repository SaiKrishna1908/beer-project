package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class BreweryControllerIT extends BaseIT {

    private static final String JSONURL = "/brewery/api/v1/breweries";
    private static final String URL = "/brewery/breweries";

    @Test
    void listBreweryADMIN() throws Exception {
        mockMvc.perform(get(URL).with(httpBasic(ADMIN,ADMIN_PASSWORD))).andExpect(status().is2xxSuccessful());
    }


    @Test
    void listBreweryCUSTOMER() throws Exception {
        mockMvc.perform(get(URL).with(httpBasic(CUSTOMER,CUSTOMER_PASSWORD))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweryUSER() throws Exception {
        mockMvc.perform(get(URL).with(httpBasic(USER,USER_PASSWORD))).andExpect(status().isForbidden());
    }


    @Test
    void listBreweryNOAUTH() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweryJsonADMIN() throws Exception {
        mockMvc.perform(get(JSONURL).with(httpBasic(ADMIN,ADMIN_PASSWORD))).andExpect(status().isOk());
    }


    @Test
    void listBreweryJsonCUSTOMER() throws Exception {
        mockMvc.perform(get(JSONURL).with(httpBasic(CUSTOMER,CUSTOMER_PASSWORD))).andExpect(status().isOk());
    }

    @Test
    void listBreweryJsonUSER() throws Exception {
        mockMvc.perform(get(JSONURL).with(httpBasic(USER,USER_PASSWORD))).andExpect(status().isForbidden());
    }

    @Test
    void listBreweryJsonNOAUTH() throws Exception {
        mockMvc.perform(get(JSONURL)).andExpect(status().isUnauthorized());
    }

}
