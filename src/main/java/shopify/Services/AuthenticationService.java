package shopify.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopify.Data.DTOs.UserDTO;
import shopify.Data.DTOs.SignInDTO;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;

@Transactional
@Service
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void addUser(UserDTO user){
        User newUser = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                Role.USER
        );
        userRepository.save(newUser);
    }

    public void signIn(SignInDTO body) {

    }

    public void signIn(String username, String password) {
        try{
            Authentication auth = authenticationManager
                    .authenticate( new UsernamePasswordAuthenticationToken(username, password));
        }catch (Exception e){
            throw new UsernameNotFoundException("Username or password is incorrect");
        }
    }
}
