package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {


    @Test
    void testListCustomerADMIN() throws Exception {
        mockMvc.perform(get("/customers").with(httpBasic("krishna", "password")))
                    .andExpect(status().isOk());
    }

    @Test
    void testListCustomerUSER() throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic("qwerty", "password")))
                    .andExpect(status().isForbidden());
    }

    @Test
    void testListCustomerCUSTOMER() throws Exception{
        mockMvc.perform(get("/customers").with(httpBasic("scott","tiger")))
                    .andExpect(status().isOk());
    }

    @Test
    void testListCustomerNOADMIN() throws Exception{
        mockMvc.perform(get("/customers")).andExpect(status().isUnauthorized());
    }

    @DisplayName("User Creation Test's")
    @Nested
    class Creation {

        static final String url = "/customers/new";


        @Test
        void processCreationFormTest() throws Exception {
            mockMvc.perform(post(url).param("customerName","abcd")
                        .with(httpBasic("krishna","password"))).andExpect(status().is3xxRedirection());
        }

        @Test
        void processCreationFormNotAuth() throws Exception {
            mockMvc.perform(post(url).param("customerName","abcd")
                    .with(httpBasic("qwerty","password"))).andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNoAuth() throws Exception {
            mockMvc.perform(post(url).param("customerName","abcd")).andExpect(status().isUnauthorized());

        }
    }

}
