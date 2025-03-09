package shopify.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
    public ResponseEntity<List<ProductDTO>> fetchAll(){
        List<ProductDTO> Products = productRepository.findAll().stream()
                .map(ProductDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(Products);
    }

    @PostMapping("")
    public ResponseEntity<String > addProduct(
            @RequestPart ProductDTO productDTO,
            @RequestPart MultipartFile file) {
        try{
            productService.addProduct(productDTO,file);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> fetchProduct(@PathVariable Long id){
        log.info(id.toString());
        ProductDTO product = productService.getProduct(id);
        if (product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam String name){
        var products = productService.searchProduct(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
