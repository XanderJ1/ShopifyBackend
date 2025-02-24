package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopify.Data.Models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAuthority(String authority);
}
