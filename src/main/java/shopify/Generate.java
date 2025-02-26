package shopify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public CommandLineRunner commandLineRunner(){


        return args -> {
            Set<Role> roles = new HashSet<Role>();
            Role role = new Role("ADMIN");
            roles.add(role);
            roleRepository.save(role);
            User bash = new User("bash",
                    passwordEncoder.encode("test"), "bzakariyya@gmail.com", roles);
            userRepository.save(bash);
        };
    }

   /* @Bean
    public CommandLineRunner commandLineRunner2() throws  IOException{

        byte[] image1 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\airmax.png"));
        byte[] image2 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\airpods-max-headphones-silver.png"));
        byte[] image3 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\AirPods Max Headphones Green.H02.watermarked.2k.png"));
        byte[] image4 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\headphone1.png"));
        byte[] image5 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Html\\VueShopify\\Shopify\\src\\assets\\images\\headphone2.png"));

        Product newProduct3 =  new Product("Airmax", "Yes" ,74, "hedon", "image/png", image1);
        Product newProduct2 =  new Product("Airpods Max Headphones-silver", "Yes" ,200, "hedon", "image/png", image2);
        Product newProduct4 =  new Product("AirPods Max Headphones Green", "Yes" ,84, "hedon", "image/png", image3);
        Product newProduct1 =  new Product("Headphone1", "Yes" ,49, "hedon", "image/png", image4);
        Product newProduct5 =  new Product("Headphone2", "Yes" ,69, "hedon", "image/png", image5);

        return args -> {
            LinkedList<Product> products = new LinkedList<>();
            products.add(newProduct1);
            products.add(newProduct2);
            products.add(newProduct3);
            products.add(newProduct4);
            products.add(newProduct5);
            productRepository.saveAll(products);

        };
    }*/
}
