package shopify.Services;

    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jwt.Jwt;
    import org.springframework.stereotype.Service;
    import shopify.Data.DTOs.Frontend;
    import shopify.Data.DTOs.UserDTO;
    import shopify.Data.DTOs.SignInDTO;
    import shopify.Data.Models.Seller;
    import shopify.Data.Models.User;
    import shopify.Data.Models.Buyer;
    import shopify.Exceptions.InternalServerError;
    import shopify.Repositories.UserRepository;

    import java.util.List;

    import static shopify.Data.Models.Role.*;

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
         * This method extracts the current user's id to make operations that needs the user's ID
         * @return Long user's  ID
         */
        public Long userId() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
                throw new IllegalStateException("User not authenticated");
            }

            Object userIdClaim = jwt.getClaim("user_id");
            if (userIdClaim == null) {
                throw new IllegalStateException("User ID claim not found in JWT");
            }

            try {
                return Long.valueOf(userIdClaim.toString());
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid user ID format in JWT", e);
            }
        }


        /**
         * Adds a user to the user database
         * @param user userDTO with details to be added
         */
        public ResponseEntity<String > addUser(UserDTO user){
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
            else if (user.getRole().equalsIgnoreCase(BUYER.toString())) {
                Buyer newUser = new Buyer(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        BUYER
                );
                userRepository.save(newUser);
            }else{
                User newUser = new Buyer(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        BUYER
                );
                userRepository.save(newUser);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
        }

        /**
         * Logs a user in
         * @param body contains user's username and password
         */
        public ResponseEntity<Frontend> signIn(SignInDTO body) throws InternalServerError {
            try{
                User user = userRepository.findByUsername(body.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found"));
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));
                String token = tokenService.generate(user);
                Frontend frontend = new Frontend(user.getRole().toString(), token);
                return ResponseEntity.ok(frontend);
            }catch (UsernameNotFoundException e){
                throw new BadCredentialsException("Username and password is incorrect");
            }catch (Exception e){
                throw new InternalServerError("Something went wrong");
            }
        }
    }
