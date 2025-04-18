package shopify.Services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shopify.Data.DTOs.ProductDTO;
import shopify.Data.Models.*;
import shopify.Repositories.OrderRepository;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.UserRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that handles business logic for product-related operations,
 * including creation, retrieval, and searching.
 */
@Slf4j
@Service
public class ProductService {

    private final UserService userService;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository,
                          UserService userService,
                          UserRepository userRepository, OrderRepository orderRepository){
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Adds a new product to the database and associates it with the creator
     * @param body The details about the product
     * @param file The image file of the product
     * @throws IOException If there's an error reading the file
     */
    @Transactional
    public ResponseEntity<String> addProduct(ProductDTO body, MultipartFile file) throws IOException {
        String username = userService.authenticatedUsername();
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user instanceof Seller) {
            Seller seller = (Seller) user;

            Product product = new Product(
                    body.getName(), body.getDescription(),
                    body.getPrice(), file.getOriginalFilename(),
                    file.getContentType(), file.getBytes());
            product.setSeller(seller);
            seller.getProducts().add(product);
            productRepository.save(product);

            userRepository.save(seller);

            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");

        }
        if (user instanceof Buyer){
            return ResponseEntity.status(HttpStatus.CREATED).body("Create a seller account to add products");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" Something went wrong please try again later");
    }

    /**
     * Fetches a single product by its ID
     * @param id The ID of the productI
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
        List<Product> products = productRepository.findBySeller_Id(id);
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

    @Transactional
    public ResponseEntity<String> addToCart(Long userId, Long productId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(userOptional.isPresent() && productOptional.isPresent()){
            Buyer buyer = (Buyer) userOptional.get();
            Product product = productOptional.get();
            buyer.getCart().add(product);
            userRepository.save(buyer);

            product.getBuyers().add(buyer);
            productRepository.save(product);
            return ResponseEntity.ok().body("Added to cart");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not add to cart");
    }

    @Transactional
    public ResponseEntity<List<ProductDTO>> myCart(Long aLong) {
        User user = userRepository.findById(aLong)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));

        if (!(user instanceof Buyer)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of(new ProductDTO()));
        }
        Buyer buyer = (Buyer) user;
        List<Product> products = buyer.getCart();
        return ResponseEntity.status(HttpStatus.OK).body(products.stream().map(ProductDTO::new).collect(Collectors.toList()));
    }

    public ResponseEntity<String> buy(Long aLong, Long productId) {

        try {
            User user = userRepository.findById(3L).orElseThrow(RuntimeException::new);
            Product product = productRepository.findById(6L).orElseThrow(RuntimeException::new);
            System.out.println(user.getUsername() + "<------>-" + product.getName());
            Buyer buyer = (Buyer) user;
            Set<Product> products = new HashSet<>();
            products.add(product);
            Order order = new Order(products, product.getSeller(), buyer);
            order.setStatus(Status.INITIATED);
            orderRepository.save(order);

            return ResponseEntity.status(HttpStatus.CREATED).body("Order Created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Transactional
    public ResponseEntity<List<Order>> fetchSellerOrders(Long aLong) {

        User user = userRepository.findById(aLong).orElseThrow(RuntimeException::new);
        Seller seller = (Seller) user;
        List<Order> orders = seller.getOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Transactional
    public ResponseEntity<List<Order>> fetchBuyerOrders(Long aLong) {

        User user = userRepository.findById(aLong).orElseThrow(RuntimeException::new);
        Buyer buyer = (Buyer) user;
        List<Order> orders = buyer.getOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
