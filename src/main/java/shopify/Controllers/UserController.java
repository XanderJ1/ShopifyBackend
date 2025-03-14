package shopify.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopify.Data.DTOs.ProductDTO;
import shopify.Data.DTOs.UserDTO;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;
import shopify.Services.AuthenticationService;
import shopify.Services.ProductService;
import shopify.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final private UserRepository userRepository;
    final private ProductService productService;
    final private UserService userService;

    public  UserController(
            UserRepository userRepository,
            ProductService productService,
            AuthenticationService authenticationService,
            UserService userService){
        this.userRepository = userRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> fetchUsers(){
        return userService.getAll();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getMyProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getMyProduct(id));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }
}
