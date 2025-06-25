package shopify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopify.Data.Models.Product;
import shopify.Data.Models.Role;
import shopify.Data.Models.Seller;
import shopify.Data.Models.User;
import shopify.Repositories.ProductRepository;
import shopify.Repositories.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Optional;

@Configuration
public class Generate {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Profile("docker")
    @Bean
    public CommandLineRunner commandLineRunner1() throws Exception{
        Seller bash = new Seller("bash",
                passwordEncoder.encode("test"), "bzakariyya@gmail.com", Role.ADMIN);
        userRepository.save(bash);
        return args -> {
            userRepository.save(bash);
        };
    }

    @Profile("local")
    @Bean
    public CommandLineRunner commandLineRunner2() throws Exception {
        Seller bash = new Seller("bash",
                passwordEncoder.encode("test"), "bzakariyya@gmail.com", Role.ADMIN);
        userRepository.save(bash);

        byte[] image1 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\kick.png"));
        byte[] image2 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\airpods-max-headphones-silver.png"));
        byte[] image3 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\AirPods Max Headphones Green.H02.watermarked.2k.png"));
        byte[] image4 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\headphone1.png"));
        byte[] image5 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\headphone2.png"));
//
        byte[] image6 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\watches.jpg"));
        byte[] image7 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\iphone.jpg"));
        byte[] image8 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\puma.png"));
        byte[] image9 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\beauty.jpg"));
        byte[] image10 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\airpod.png"));
        byte[] image11 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\vision_pro.png"));
        byte[] image12 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\watch1.png"));
        byte[] image13 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\macbook.png"));
        byte[] image14 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\ipad.png"));
        byte[] image15 = Files.readAllBytes(Path.of("C:\\Users\\user\\Desktop\\Code Directory\\Shopify\\src\\assets\\images\\track.png"));

        LinkedList<Product> products = new LinkedList<>();
        Optional<User> savedAdmin = userRepository.findByUsername("bash");
        if (savedAdmin.isPresent()) {
            Seller admin = (Seller) savedAdmin.get();
            products.add(new Product("Airmax", "Yes", 74, admin, "hedon", "image/png", image1));
            products.add(new Product("Airpods Max Headphones-silver", "Yes", 200, admin, "hedon", "image/png", image2));
            products.add(new Product("AirPods Max Headphones Green", "Yes", 84, admin, "hedon", "image/png", image3));
            products.add(new Product("Headphone1", "Yes", 49, admin, "hedon", "image/png", image4));
            products.add(new Product("Headphone2", "Yes", 69, admin, "hedon", "image/png", image5));
            products.add(new Product("Rolex daytona", "Yes", 69, admin, "hedon", "image/png", image6));
            products.add(new Product("Iphone 11 pro", "Yes", 69, admin, "hedon", "image/png", image7));
            products.add(new Product("Nike Air", "Yes", 69, admin, "hedon", "image/png", image8));
            products.add(new Product("Volce lipstick", "Yes", 69, admin, "hedon", "image/png", image9));
            products.add(new Product("Airpods pro", "Yes", 69, admin, "hedon", "image/png", image10));
            products.add(new Product("Vision Pro", "Yes", 69, admin, "hedon", "image/png", image11));
            products.add(new Product("Richard mille", "Yes", 69, admin, "hedon", "image/png", image12));
            products.add(new Product("Macbook pro", "Yes", 69, admin, "hedon", "image/png", image13));
            products.add(new Product("Ipad pro", "Yes", 69, "hedon", "image/png", image14));
            products.add(new Product("New balance", "Yes", 69, "hedon", "image/png", image15));
            products.add(new Product("Oraimo Lex 23 ", "Yes", 69, "hedon", "image/png", image5));
        }
        else {
            products.add(new Product("Airmax", "Yes", 74, "hedon", "image/png", image1));
            products.add(new Product("Airpods Max Headphones-silver", "Yes", 200, "hedon", "image/png", image2));
            products.add(new Product("AirPods Max Headphones Green", "Yes", 84, "hedon", "image/png", image3));
            products.add(new Product("Headphone1", "Yes", 49, "hedon", "image/png", image4));
            products.add(new Product("Headphone2", "Yes", 69, "hedon", "image/png", image5));
            products.add(new Product("Rolex daytona", "Yes", 69, "hedon", "image/png", image6));
            products.add(new Product("Iphone 11 pro", "Yes", 69, "hedon", "image/png", image7));
            products.add(new Product("Nike Air", "Yes", 69, "hedon", "image/png", image8));
            products.add(new Product("Volce lipstick", "Yes", 69, "hedon", "image/png", image9));
            products.add(new Product("Airpods pro", "Yes", 69, "hedon", "image/png", image10));
            products.add(new Product("Vision Pro", "Yes", 69, "hedon", "image/png", image11));
            products.add(new Product("Richard mille", "Yes", 69, "hedon", "image/png", image12));
            products.add(new Product("Macbook pro", "Yes", 69, "hedon", "image/png", image13));
            products.add(new Product("Ipad pro", "Yes", 69, "hedon", "image/png", image14));
            products.add(new Product("New balance", "Yes", 69, "hedon", "image/png", image15));
            products.add(new Product("Oraimo Lex 23 ", "Yes", 69, "hedon", "image/png", image5));

        }
        return args -> {
            productRepository.saveAll(products);

        };
    }
}
