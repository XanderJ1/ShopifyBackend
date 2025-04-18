package shopify.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shopify.Data.Models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
