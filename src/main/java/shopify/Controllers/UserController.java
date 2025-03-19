package shopify.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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

    final private ProductService productService;
    final private UserService userService;

    public  UserController(
            ProductService productService,
            AuthenticationService authenticationService,
            UserService userService){
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * This method extracts the current user's id to make operations that needs the user's ID
     * @return Long user's  ID
     */
    public Long userId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth + "Hash" + auth.getPrincipal());
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("User not authenticated");
        }

        Object userIdClaim = jwt.getClaim("user_id");
        if (userIdClaim == null) {
            throw new IllegalStateException("User ID claim not found in JWT");
        }

        try {
            return Long.valueOf(userIdClaim.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid user ID format in JWT", e);
        }
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
     * @return List of products
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getMyProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getMyProduct(userId()));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<ProductDTO>> myCart(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.myCart(2L));
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
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId()));
    }
}
