package shopify.Data.DTOs;

import lombok.Data;

@Data
public class SignInDTO {

    String username;
    String password;

    public SignInDTO(){

    }

    public SignInDTO(String username, String password){
        this.username = username;
        this.password = password;
    }
}
