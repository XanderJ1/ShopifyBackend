package shopify.Data.DTOs;

import jakarta.validation.constraints.Email;
import lombok.Data;
import shopify.Data.Models.User;

@Data
public class UserDTO {
    private String username;
    private String password;
    @Email
    private String email;
    private String role;

    UserDTO(){

    }

    public UserDTO(String role){
        this.role = role;
    }

    public UserDTO(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserDTO(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDTO(User user) {
        if (user != null){
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole().toString();
        }
    }
}
