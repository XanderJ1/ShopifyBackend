package shopify.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

/**
 * REST controller for handling product-related operations.
 */
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

    /**
     * Fetches all products in the database depending on the request parameters
     * @param sort sorts products according to sort value
     * @param filter filters products based on the filter parameters
     * @return Lists of ptoducts
     */
    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> fetchAll(@RequestParam(required = false) String sort, @RequestParam(required = false) String filter){
        if (sort != null && filter != null){

        }
        if (sort != null){
            List<ProductDTO> Products = productRepository.findAll(Sort.by(sort)).stream()
                    .map(ProductDTO::new).collect(Collectors.toList());
        }
        if (filter != null){

        }
        List<ProductDTO> Products = productRepository.findAll().stream()
                .map(ProductDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok(Products);
    }

    /**
     * Adds a new product to the database
     * @param productDTO the product details
     * @param file the image file of the product
     * @return HTTP status code indicating success or failure
     */
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

    /**
     * Fetches a product by its id
     * @param id ID of the product
     * @return the product or a 404 HTTP status code
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> fetchProduct(@PathVariable Long id){
        log.info(id.toString());
        ProductDTO product = productService.getProduct(id);
        log.info("Fetched product: {}", product);
        if (product != null){
            log.info(product.getImageData() + " " + product.getImageType(), product);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Search for products
     * @param name name of the product
     * @return list of products
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam String name){
        var products = productService.searchProduct(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
