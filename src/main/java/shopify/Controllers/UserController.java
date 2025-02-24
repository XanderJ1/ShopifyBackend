package shopify.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopify.Data.DTOs.RegistrationDTO;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;
import shopify.Services.AuthenticationService;
import shopify.Services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final private UserRepository userRepository;

    public  UserController(UserRepository userRepository, AuthenticationService authenticationService){
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }





}
