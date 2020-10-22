package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.BreweryRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {

    @Autowired
    WebApplicationContext wac;


    public MockMvc mockMvc;





    protected  static final String ADMIN = "krishna";
    protected  static final String ADMIN_PASSWORD = "password";

    protected  static final String USER = "qwerty";
    protected  static final String USER_PASSWORD = "password";

    protected  static final String CUSTOMER = "scott";
    protected static final String CUSTOMER_PASSWORD = "tiger";

    @BeforeEach
    public void init(){

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

//    public static Stream<Arguments> getStreamAdminCustomer(){
//        return Stream.of(Arguments.of("krishna", "password"),
//                        Arguments.of("scott", "tiger"));
//    }
}
