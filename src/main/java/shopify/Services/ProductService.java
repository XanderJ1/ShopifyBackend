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
import java.util.HashSet;
import java.util.Set;

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

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
