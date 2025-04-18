package shopify.Data.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User{

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
    @OneToMany(mappedBy = "seller")
    private List<Order> orders = new ArrayList<>();

    public Seller(){
        super();
    }

    public Seller(String username, String password, String email, Role role){
        super(username, password, email,role);

    }
}
