package shopify.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopify.Data.DTOs.UserDTO;
import shopify.Data.DTOs.SignInDTO;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;
import shopify.Services.AuthenticationService;
import shopify.Services.TokenService;
import shopify.Services.UserService;

import java.util.Optional;

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

    @PostMapping("/register")
    public ResponseEntity<String > register(@RequestBody UserDTO user){
        if (user.getUsername() != null && user.getPassword() != null){
            Optional<User> newUser = userRepository.findByUsername(user.getUsername());
            if (newUser.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
            else {
                authenticationService.addUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String > signIn(@RequestBody SignInDTO body){
        try{
            User user = userRepository.findByUsername(body.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            authenticationService.signIn(body.getUsername(), body.getPassword());
            return ResponseEntity.ok(tokenService.generate(user));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is incorrect");
        }
    }
}
