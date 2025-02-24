package shopify.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shopify.Data.DTOs.ProductDTO;
import shopify.Data.Models.Product;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.UserRepository;
import shopify.Services.ProductService;
import shopify.Services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("")
    public ResponseEntity<List<Product>> fetchAll(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/addProduct")
    public ResponseEntity<String > addProduct(@RequestPart ProductDTO productDTO, @RequestPart MultipartFile file) {
        try{
            productService.addProduct(productDTO,file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/fetchProduct/{id}")
    public ResponseEntity<Product> fetchProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        if (product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
