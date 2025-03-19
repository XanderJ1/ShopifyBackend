    package shopify.Data.Models;


    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.Data;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "appUser", discriminatorType = DiscriminatorType.STRING)
    @Entity
    @Data
    @Table(name = "appUser")
    public class User implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String password;
        private String email;

        @Enumerated(EnumType.STRING)
        private Role role;

        public User(){

        }
        public User(String username, String password, String email, Role role){
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;

        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword(){
            return password;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();  // Exclude assignedUser from hashCode calculation
        }

    }
