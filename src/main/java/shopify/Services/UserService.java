package shopify.Services;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shopify.Data.DTOs.UserDTO;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userRepository.findAll()
                .stream().map(UserDTO::new).collect(Collectors.toList()));
    }

    public String authenticatedUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String update(User user) {
        Optional<User> user1 = userRepository.findById(user.getId());
        User updatedUser = new User();
        if (user1.isEmpty()) {
            return "User does not exist";
        }else{
            updatedUser = user1.get();

            if (updatedUser.getUsername() != null) {
                updatedUser.setUsername(user.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                updatedUser.setEmail(user.getEmail());
            }
            if (updatedUser.getRole() != null) {
                updatedUser.setRole(user.getRole());
            }

            userRepository.save(updatedUser);
            return "User is updated";
        }
    }

    public String deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            return "User does not exist";
        }
        else {
            userRepository.delete(user.get());
            return "User has been deleted";
        }
    }
}
