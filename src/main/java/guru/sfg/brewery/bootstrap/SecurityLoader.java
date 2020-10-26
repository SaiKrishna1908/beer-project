package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Slf4j
public class SecurityLoader implements CommandLineRunner {


    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String CUSTOMER_ROLE = "ROLE_CUSTOMER";
    private static final String USER_ROLE = "ROLE_USER";


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if(authorityRepository.count() == 0)
                loadUserData();
    }

    private void loadUserData(){

        //Beer Operations

        Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());



        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());


        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer,
                updateBeer, readBeer,deleteBeer
                ,createBrewery,updateBrewery,readBrewery,deleteBrewery,
                readCustomer,updateCustomer,deleteCustomer,createCustomer)));

        customerRole.setAuthorities( new HashSet<>(Set.of(readBeer,readCustomer,readBrewery)));

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

        log.debug("Loaded User data succussfully");


    }
}
