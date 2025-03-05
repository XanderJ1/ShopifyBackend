package shopify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopify.Data.Models.Product;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.RoleRepository;
import shopify.Repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

@Configuration
public class Generate {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() throws Exception {

            Set<Role> roles = new HashSet<Role>();
            Role role = new Role("ADMIN");
            roles.add(role);
            roleRepository.save(role);
            User bash = new User("bash",
                    passwordEncoder.encode("test"), "bzakariyya@gmail.com", roles);
            userRepository.save(bash);

        byte[] image1 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\airmax.png"));
        byte[] image2 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\airpods-max-headphones-silver.png"));
        byte[] image3 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\AirPods Max Headphones Green.H02.watermarked.2k.png"));
        byte[] image4 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\headphone1.png"));
        byte[] image5 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\headphone2.png"));

        LinkedList<Product> products = new LinkedList<>();
        Optional<User> savedAdmin = userRepository.findByUsername("bash");
        if (savedAdmin.isPresent()) {
            User admin = savedAdmin.get();
            products.add(new Product("Airmax", "Yes", 74, admin, "hedon", "image/png", image1));
            products.add(new Product("Airpods Max Headphones-silver", "Yes", 200, admin, "hedon", "image/png", image2));
            products.add(new Product("AirPods Max Headphones Green", "Yes", 84, admin, "hedon", "image/png", image3));
            products.add(new Product("Headphone1", "Yes", 49, admin, "hedon", "image/png", image4));
            products.add(new Product("Headphone2", "Yes", 69, admin, "hedon", "image/png", image5));
        }
        else {
            products.add(new Product("Airmax", "Yes", 74, "hedon", "image/png", image1));
            products.add(new Product("Airpods Max Headphones-silver", "Yes", 200, "hedon", "image/png", image2));
            products.add(new Product("AirPods Max Headphones Green", "Yes", 84, "hedon", "image/png", image3));
            products.add(new Product("Headphone1", "Yes", 49, "hedon", "image/png", image4));
            products.add(new Product("Headphone2", "Yes", 69, "hedon", "image/png", image5));
        }
        return args -> {
            productRepository.saveAll(products);

        };
    }
}
