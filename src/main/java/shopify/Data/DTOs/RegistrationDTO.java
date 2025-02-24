package shopify.Data.DTOs;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String role;

    RegistrationDTO(){

    }

    public RegistrationDTO(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public RegistrationDTO(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
