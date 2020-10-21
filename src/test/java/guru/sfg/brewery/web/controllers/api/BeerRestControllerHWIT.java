package guru.sfg.brewery.web.controllers.api;

//Homework

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
 class BeerRestControllerHWIT extends BaseIT {

    private static final String URL = "/api/v1/beer/b8cf34de-1488-4abf-ba6d-1aba3e0b99dc";
    private static final String KEY = "Api-Key";
    private static final String VALUE = "Api-Secret";


    @Test
    void deleteBeerBadCred() throws  Exception{
        mockMvc.perform(delete(URL).param(KEY,"krishna").param(VALUE,
                "password1")).andExpect(status().isUnauthorized());

    }

    @Test
    void deleteBeer() throws Exception{
        mockMvc.perform(delete(URL).param(KEY,"krishna").param(VALUE,"password"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception{
        mockMvc.perform(delete(URL).param(KEY,"krishna").param(VALUE,"password"))
                .andExpect(status().is2xxSuccessful());
    }
}
