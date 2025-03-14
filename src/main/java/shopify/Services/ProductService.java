package shopify.Services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shopify.Data.DTOs.ProductDTO;
import shopify.Data.Models.Product;
import shopify.Data.Models.User;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.UserRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that handles business logic for product-related operations,
 * including creation, retrieval, and searching.
 */
@Service
public class ProductService {

    private final UserService userService;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository,
                          UserService userService,
                          UserRepository userRepository){
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Adds a new product to the database and associates it with the creator
     * @param body The details about the product
     * @param file The image file of the product
     * @throws IOException If there's an error reading the file
     */
    @Transactional
    public void addProduct(ProductDTO body, MultipartFile file) throws IOException {
        String username = userService.authenticatedUsername();
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product newProduct = new Product(body.getName(), body.getDescription(), body.getPrice(), user);
        newProduct.setImageName(file.getName());
        newProduct.setImageType(file.getContentType());
        newProduct.setImageData(file.getBytes());
        Set<Product> products = user.getProducts();
        products.add(newProduct);
        user.setProducts(products);
        productRepository.save(newProduct);
    }

    /**
     * Fetches a single product by its ID
     * @param id The ID of the product
     * @return The product or null if not found
     */
    @Transactional
    public ProductDTO getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return null;
        }
        else
            return new ProductDTO(product.get());
    }

    /**
     * Fetches products created by a user
     * @param id The ID of the user
     * @return list of products
     */
    @Transactional
    public List<ProductDTO> getMyProduct(Long id) {
        List<Product> products = productRepository.findByAssignedUser_Id(id);
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    /**
     * Search for products by name
     * @param name name of the product
     * @return list of matching productDTOs
     */
    @Transactional
    public List<ProductDTO> searchProduct(String name) {
        return productRepository.search(name.toLowerCase())
                .stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }
}
