    /**
     * This package contains business logic of controllers
     */
    package shopify.Services;

    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import shopify.Data.DTOs.UserDTO;
    import shopify.Data.DTOs.SignInDTO;
    import shopify.Data.Models.Seller;
    import shopify.Data.Models.User;
    import shopify.Data.Models.Buyer;
    import shopify.Repositories.UserRepository;

    import java.util.List;

    import static shopify.Data.Models.Role.*;

    @Transactional
    @Service
    public class AuthenticationService {


        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final TokenService tokenService;

        @Autowired
        AuthenticationManager authenticationManager;


        public AuthenticationService(
                PasswordEncoder passwordEncoder,
                UserRepository userRepository,
                TokenService tokenService
                ){
            this.passwordEncoder = passwordEncoder;
            this.userRepository = userRepository;
            this.tokenService = tokenService;
        }

        /**
         * Adds a user to the user database
         * @param user userDTO with details to be added
         */
        public void addUser(UserDTO user){
            // If user is a seller
            if (user.getRole().equalsIgnoreCase(SELLER.toString())) {
                Seller newUser = new Seller(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        SELLER
                );
                userRepository.save(newUser);
            }
            // If user has role a user
            else if (user.getRole().equalsIgnoreCase(USER.toString())) {
                Buyer newUser = new Buyer(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        USER
                );
                userRepository.save(newUser);
            }else{
                User newUser = new Buyer(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        USER
                );
                userRepository.save(newUser);
            }
        }

        public void signIn(SignInDTO body) {

        }

        /**
         * Logs a user in
         * @param username user's username
         * @param password user's password
         */
        public ResponseEntity<List<String>> signIn(String username, String password, User user) {
            try{
                Authentication auth = authenticationManager
                        .authenticate( new UsernamePasswordAuthenticationToken(username, password));
                return ResponseEntity.ok(List.of(tokenService.generate(user), user.getId().toString()));
            }catch (UsernameNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("User not found"));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of("Username or password is incorrect"));
            }
        }
    }
