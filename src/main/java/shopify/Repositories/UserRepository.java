package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopify.Data.Models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
