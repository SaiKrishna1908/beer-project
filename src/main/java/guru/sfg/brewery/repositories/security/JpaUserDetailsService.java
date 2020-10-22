package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsername(username).orElseThrow( () ->{
            return new UsernameNotFoundException("User name:" + username+ " not found");
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getEnabled(), user.getAccountNonExpired(), user.getCredentialNonExpired(),
                user.getAccountNonLocked(), convertToSpringAuthrorities(user.getAuthorities()));       
    }

    private Collection<? extends GrantedAuthority> convertToSpringAuthrorities(Set<Authority> authorities) {
        if(authorities != null && authorities.size() > 0){
            return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        else{
            return new HashSet<>();
        }
    }
}
