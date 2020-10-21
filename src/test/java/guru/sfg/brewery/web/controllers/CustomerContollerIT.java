package guru.sfg.brewery.web.controllers;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerContollerIT extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @Disabled
    @MethodSource("guru.sfg.brewery.web.controllers.BaseIT#getStreamAdminCustomer")
    public void testListCustomersAUTH(String user, String password) throws Exception{

        mockMvc.perform(get("/customers").with(httpBasic(user, password))).andExpect(status().isOk());
    }

    @Test
    @Disabled
     public void testListCustomersNOTAUTH() throws Exception{

        mockMvc.perform(get("/customers").with(httpBasic("krishna","password")))
                    .andExpect(status().isForbidden());
    }
}
