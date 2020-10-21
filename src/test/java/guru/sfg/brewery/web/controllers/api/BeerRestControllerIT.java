package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerBadCred() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc")
                .header("Api-Key","krishna").header("Api-Secret","password1"))
                .andExpect(status().isUnauthorized());

    }
    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc")
                .header("Api-Key", "krishna").header("Api-Secret","password"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc")
        .with(httpBasic("krishna","password"))).andExpect(status().is2xxSuccessful());
    }
    @Test
    void deleteBeerHttpBasicUserRole() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc")
                .with(httpBasic("qwerty","password"))).andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerHttpBasicCustomerRole() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc")
                .with(httpBasic("scott","tiger"))).andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerNoAauth() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    void findBeersAdminRole() throws Exception{
        mockMvc.perform(get("/api/v1/beer/").with(httpBasic("krishna","password")))
                .andExpect(status().isOk());

    }


    @Test
    void findBeersCustomerRole() throws Exception{
        mockMvc.perform(get("/api/v1/beer/").with(httpBasic("scott","tiger")))
                .andExpect(status().isOk());

    }


    @Test
    void findBeersUserRole() throws Exception{
        mockMvc.perform(get("/api/v1/beer/").with(httpBasic("qwerty","password")))
                .andExpect(status().isForbidden());

    }



    @Test
    void findBeerByIdAdminRole() throws Exception{

        mockMvc.perform(get("/api/v1/beer/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("krishna","password" )))
                .andExpect(status().isOk());
    }


    @Test
    void findBeerByCustomerRole() throws Exception{

        mockMvc.perform(get("/api/v1/beer/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }


    @Test
    void findBeerByUserRole() throws Exception{

        mockMvc.perform(get("/api/v1/beer/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("qwerty", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void findBeerByUpcAdminRoler() throws Exception{

        mockMvc.perform(get("/api/v1/beerUpc/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("krishna", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcCustomerRole() throws Exception{

        mockMvc.perform(get("/api/v1/beerUpc/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcUserRole() throws Exception{

        mockMvc.perform(get("/api/v1/beerUpc/04d33b1f-424c-4a75-8e78-0dda517d2fdc")
                .with(httpBasic("qwerty", "password")))
                .andExpect(status().isForbidden());
    }




}
