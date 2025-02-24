package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopify.Data.Models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
