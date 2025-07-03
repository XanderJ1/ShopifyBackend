package shopify.Controllers;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopify.Data.DTOs.Frontend;
import shopify.Data.DTOs.UserDTO;
import shopify.Data.DTOs.SignInDTO;
import shopify.Data.Models.User;
import shopify.Exceptions.InternalServerError;
import shopify.Repositories.UserRepository;
import shopify.Services.AuthenticationService;
import shopify.Services.TokenService;
import shopify.Services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 *  Rest controller for handling authentication-related operations such as
 *  user registration and login.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final private UserRepository userRepository;

    final private UserService userService;
    final private TokenService tokenService;

    final private AuthenticationService authenticationService;

    public  AuthenticationController(
            UserRepository userRepository,
            AuthenticationService authenticationService,
            TokenService tokenService,
            UserService userService){
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    /**
     * Registers a new user
     * @param user UserDTO with details of user to be registered
     * @return a ResponseEntity with HTTP status code:
     * <br> 201 (Created) if registration is successful.
     * <br> 400 (Bad Request) if the user already exists or required fields are missing.
     */
    @PostMapping("/register")
    public ResponseEntity<String > register(@RequestBody UserDTO user){
        if (user.getUsername() != null && user.getPassword() != null){
            Optional<User> newUser = userRepository.findByUsername(user.getUsername());
            if (newUser.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
            else {
                return authenticationService.addUser(user);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter username or password");
    }

    /**
     * Logs a user in
     * @param body SignInDTO containing the user's username and password
     * @return Response with HTTP status code:
     * <br> 200 (OK)  and JWT token and user's id on success.
     * <br> 400 (Bad Request) and failure message if authentication fails
     */
    @PostMapping("/signIn")
    public ResponseEntity<Frontend> signIn(@RequestBody SignInDTO body) throws InternalServerError {
        return authenticationService.signIn(body);
    }
}
