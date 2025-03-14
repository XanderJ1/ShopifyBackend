/**
 * Package containing repositories that extend JPARepository to perform CRUD operations
 */
package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopify.Data.Models.User;

import java.util.Optional;

/**
 * Repository interface implementing JpaRepository to perform database operations (CRUD)
 * on the user database
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     *  retrieves a user from the database by its username
     * @param username The username of the user
     * @return An Optional containing the user
     */
    Optional<User> findByUsername(String username);
}
