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


/**
 *  Rest Controller that handles user-related operations such as fetching users,
 *  updating user details, deleting users, and retrieving user-associated product
 */
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

    /**
     * Fetches all users from the database
     * @return List of all users
     */
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> fetchUsers(){
        return userService.getAll();
    }

    /**
     * Fetches the products associated with a specific user
     * @param id The ID of the user
     * @return List of products
     */
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getMyProduct(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getMyProduct(id));
    }

    /**
     * Updates a user's details
     * @param user details of the user to be updated
     * @return a success or failure message if the update fails
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.update(user));
    }

    /**
     * Deletes a user from the database
     * @param id ID of the user to be deleted
     * @return a success or failure message if the update fails
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }
}
