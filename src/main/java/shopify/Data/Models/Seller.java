package shopify.Data.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User{

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    public Seller(){
        super();
    }

    public Seller(String username, String password, String email, Role role){
        super(username, password, email,role);

    }
}
