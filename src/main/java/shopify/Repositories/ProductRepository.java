package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shopify.Data.Models.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface implementing JpaRepository to perform database operations (CRUD)
 * on the products database
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Fetches a product by the user's ID
     * @param userId The ID of the user
     * @return A product
     */
    List<Product> findByAssignedUser_Id(Long userId);

    /**
     * Search for a product by its name
     * @param name Name of the product
     * @return A list of product matching the name
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> search(@Param("name") String name);
}
