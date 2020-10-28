/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";


    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String CUSTOMER_ROLE = "ROLE_CUSTOMER";
    private static final String USER_ROLE = "ROLE_USER";

    private static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    private static final String DUNEBIN_DISTRIBUTIING = "Dunebin Distributing";
    private static final String KEY_WEST_DISTRIBUTING = "Key West Distributing";


    private final PasswordEncoder passwordEncoder;


    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        loadUserData();
        loadBreweryData();
        loadTastingRoomData();
        loadCustomerData();
    }

    private void loadCustomerData() {

        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();


        Customer stPeteCustomer = customerRepository.save(Customer.builder().customerName(ST_PETE_DISTRIBUTING)
                                    .apiKey(UUID.randomUUID()).build());

        Customer dunebinCustomer = customerRepository.save(Customer.builder().customerName(DUNEBIN_DISTRIBUTIING)
                .apiKey(UUID.randomUUID()).build());

        Customer keywestCustomer = customerRepository.save(Customer.builder().customerName(KEY_WEST_DISTRIBUTING)
                .apiKey(UUID.randomUUID()).build());

        User stPeteUser = userRepository.save(User.builder().username("stpete")
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(customerRole).build());

        User dunedinUser = userRepository.save(User.builder().username("dunedin")
                .password(passwordEncoder.encode("password"))
                .customer(dunebinCustomer)
                .role(customerRole).build());

        User keywest = userRepository.save(User.builder().username("keywest")
                .password(passwordEncoder.encode("password"))
                .customer(keywestCustomer)
                .role(customerRole).build());

        createOrder(stPeteCustomer);
        createOrder(dunebinCustomer);
        createOrder(keywestCustomer);

    }

    private BeerOrder createOrder(Customer customer) {

        return  beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
    }


    private void loadTastingRoomData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }

    private void loadUserData(){

        //Beer Operations

        //beer auth
        Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        //customer brewery
        Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        //customer auths
        Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        //beerorder

        Authority createOrder = authorityRepository.save(Authority.builder().permission("order.create").build());
        Authority updateOrder = authorityRepository.save(Authority.builder().permission("order.update").build());
        Authority deleteOrder = authorityRepository.save(Authority.builder().permission("order.delete").build());
        Authority readOrder = authorityRepository.save(Authority.builder().permission("order.read").build());

        //customer beer

        Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.create").build());
        Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.update").build());
        Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.delete").build());
        Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.order.read").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());


        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer,
                updateBeer, readBeer,deleteBeer
                ,createBrewery,updateBrewery,readBrewery,deleteBrewery,
                readCustomer,updateCustomer,deleteCustomer,createCustomer
                , createOrder, updateOrder, deleteOrder, readOrder)));

        customerRole.setAuthorities( new HashSet<>(Set.of(readBeer,readCustomer,readBrewery,createOrderCustomer
                ,   updateOrderCustomer, readOrderCustomer, deleteOrderCustomer)));

        userRole.setAuthorities( new HashSet<>(Set.of(readBeer)));


        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        userRepository.save(User.builder().username("jack").password(passwordEncoder.encode("password1"))
                .role(adminRole).build());

        User rose = User.builder().username("rose").password(passwordEncoder.encode("password2")).
                accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true).enabled(true).role(customerRole).build();

        User willow = User.builder().username("dark").password(passwordEncoder.encode("password3"))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialNonExpired(true)
                .enabled(true)
                .role(userRole)
                .build();

        User krishna = User.builder().username("krishna")
                .password(passwordEncoder.encode("password"))
                .accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true)
                .enabled(true)
                .role(adminRole)
                .build();

        User scott = User.builder().username("scott")
                .password(passwordEncoder.encode("tiger"))
                .accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true)
                .enabled(true).role(customerRole).build();

        User qwerty = User.builder().username("qwerty")
                .password(passwordEncoder.encode("password"))
                .accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true)
                .enabled(true).role(userRole).build();




        userRepository.save(rose);
        userRepository.save(willow);
        userRepository.save(krishna);
        userRepository.save(scott);
        userRepository.save(qwerty);

        log.debug("Loaded User data successfully");


    }
}
