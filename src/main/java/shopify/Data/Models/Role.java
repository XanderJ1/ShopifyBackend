package shopify.Data.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    public Role(){

    }
    public Role(String role){
        this.authority = role;
    }

    public void setAuthority(String authority){
        this.authority = authority;
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
