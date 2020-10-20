package guru.sfg.brewery.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import  guru.sfg.brewery.domain.security.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String name);
}
