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

/**
 * Service for managing user-related operations, such as fetching,
 * updating, and deleting users.
 */
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

    /**
     * Fetches all users from the database
     * @return A ResponseEntity containing a list of all users
     */
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userRepository.findAll()
                .stream().map(UserDTO::new).collect(Collectors.toList()));
    }

    /**
     * Gets the username of the currently logged-in user
     * @return username of the user
     */
    public String authenticatedUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /**
     * Update the details of a user
     * @param user user details to be updated
     * @return a success message or failure message if the update fails
     */
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

    /**
     * Deletes a user from the database
     * @param id The ID of the user to be deleted
     * @return a success message or failure message if the update fails
     */
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
