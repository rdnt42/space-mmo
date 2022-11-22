package marowak.dev.repository;

import io.micronaut.data.jpa.repository.JpaRepository;
import marowak.dev.model.User;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 22.11.2022
 * Time: 23:15
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
