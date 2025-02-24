package shopify.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopify.Data.DTOs.RegistrationDTO;
import shopify.Data.DTOs.SignInDTO;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;
import shopify.Repositories.RoleRepository;
import shopify.Repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class AuthenticationService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       RoleRepository roleRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void addUser(RegistrationDTO user){
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByAuthority(user.getRole()).
                orElseGet(() -> roleRepository.save(new Role("USER")));
        roles.add(role);
        User newUser = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                roles
        );
        userRepository.save(newUser);
    }

    public void signIn(SignInDTO body) {

    }

    public void signIn(String username, String password) {
        try{
            Authentication auth = authenticationManager
                    .authenticate( new UsernamePasswordAuthenticationToken(username, password));
        }catch (Exception e){
            throw new UsernameNotFoundException("Username or password is incorrect");
        }
    }
}
