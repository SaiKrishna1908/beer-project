package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Delete Tests")
    @Nested
    protected class DeleteTests{

        public Beer beerToDelete(){
            Random random = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("hold my bber")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(random.nextInt(9999999)))
                    .build());
        }


        @Test
        void deleteBeerHttpBasic() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("krishna","password"))).andExpect(status().is2xxSuccessful());
        }


        @Test
        void deleteBeerBadCred() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .header("Api-Key","krishna").header("Api-Secret","password1"))
                    .andExpect(status().isUnauthorized());

        }
        @Test
        void deleteBeer() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .header("Api-Key", "krishna").header("Api-Secret","password"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("qwerty","password"))).andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("scott","tiger"))).andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAauth() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }


        @Test
        void findBeerByIdAdminRole() throws Exception{

            mockMvc.perform(get("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("krishna","password" )))
                    .andExpect(status().isOk());
        }


        @Test
        void findBeerByCustomerRole() throws Exception{

            mockMvc.perform(get("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isOk());
        }




    }
    @Test
    void findBeerByUpcAdminRoler() throws Exception{

        mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                .with(httpBasic("krishna", "password")))
                .andExpect(status().isOk());
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
    void findBeerByUserRole() throws Exception{

        mockMvc.perform(get("/api/v1/beer/0631234200036")
                .with(httpBasic("qwerty", "password")))
                .andExpect(status().isForbidden());
    }


    @Test
    void findBeerByUpcCustomerRole() throws Exception{

        mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
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
