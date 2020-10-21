package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class SecurityLoader implements CommandLineRunner {


    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String CUSTOMER_ROLE = "CUSTOMER";
    private static final String USER_ROLE = "USER";


    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count() == 0)
                loadUserData();
    }

    private void loadUserData(){




        Authority admin = Authority.builder().role(ADMIN_ROLE).build();
        Authority customer = Authority.builder().role(CUSTOMER_ROLE).build();
        Authority user = Authority.builder().role(USER_ROLE).build();


        admin = authorityRepository.save(admin);
        customer = authorityRepository.save(customer);
        user = authorityRepository.save(user);



        User jack = User.builder().username("jack").password(passwordEncoder.encode("password1"))
                .accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true).enabled(true).authority(admin).build();

        User rose = User.builder().username("rose").password(passwordEncoder.encode("password2")).
                accountNonExpired(true).accountNonLocked(true)
                .credentialNonExpired(true).enabled(true).authority(customer).build();

        User willow = User.builder().username("dark").password(passwordEncoder.encode("password3"))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialNonExpired(true)
                    .enabled(true)
                    .authority(user)
                    .build();

        User krishna = User.builder().username("krishna")
                        .password(passwordEncoder.encode("password"))
                        .accountNonExpired(true).accountNonLocked(true)
                        .credentialNonExpired(true)
                        .enabled(true)
                        .authority(admin)
                        .build();

        User scott = User.builder().username("scott")
                    .password(passwordEncoder.encode("tiger"))
                    .accountNonExpired(true).accountNonLocked(true)
                    .credentialNonExpired(true)
                    .enabled(true).authority(customer).build();

        User qwerty = User.builder().username("qwerty")
                    .password(passwordEncoder.encode("password"))
                    .accountNonExpired(true).accountNonLocked(true)
                    .credentialNonExpired(true)
                    .enabled(true).authority(user).build();



        userRepository.save(jack);
        userRepository.save(rose);
        userRepository.save(willow);
        userRepository.save(krishna);
        userRepository.save(scott);
        userRepository.save(qwerty);

        log.debug("Loaded User data succussfully");


    }
}
