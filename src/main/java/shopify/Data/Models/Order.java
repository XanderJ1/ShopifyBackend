package shopify.Data.Models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();
    private String shippingAddress;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer customer;
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private Status status;
}
