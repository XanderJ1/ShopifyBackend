package shopify.Data.Models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
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
    private List<Product> products = new ArrayList<>();

    @ManyToOne()
    private Seller seller;
    private String shippingAddress;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer customer;
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Order(List<Product> products, Buyer customer) {
        this.products = products;
        this.seller = seller;
        this.customer = customer;
    }
}
