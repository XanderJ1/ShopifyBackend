package shopify.Data.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("BUYER")
public class Buyer extends User {

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "buyer_cart",
            joinColumns = @JoinColumn(name = "buyer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> cart = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Order> orders = new HashSet<>();

    public Buyer(){

    }
    public Buyer(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }
}
