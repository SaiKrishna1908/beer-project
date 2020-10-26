package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class BeerControllerIT extends BaseIT{

    @Test
//    @WithMockUser("spring")
    void findBeers() throws Exception {
        mockMvc.perform(get("/beers/find")).andExpect(status().isOk())
        .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));

    }

    @Test
    void initCreationFormAdmin() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("krishna", "password")))
                .andExpect(status().isOk()).andExpect(model().attributeExists("beer")).andExpect(
                        view().name("beers/createBeer"));



    }

    @Test
    void initCreationForScott() throws Exception{

        mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());

    }

    @Test
    void initCreationForQuerty() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("qwerty","password")))
                .andExpect(status().isForbidden());
    }


}
