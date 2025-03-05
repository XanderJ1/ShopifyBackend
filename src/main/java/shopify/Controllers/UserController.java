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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final private UserRepository userRepository;
    final private ProductService productService;

    public  UserController(UserRepository userRepository, ProductService productService, AuthenticationService authenticationService){
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> fetchUsers(){
        return ResponseEntity.ok(userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getMyProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getMyProduct(id));
    }

}
