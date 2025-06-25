package shopify.Services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import shopify.Data.Models.Buyer;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void beforeIt(){

    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void getAll() {
    }

    @Test
    void authenticatedUsername() {
        String expectedUsername = "Juliana";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(expectedUsername, "test");
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String result = userService.authenticatedUsername();
        assertEquals(expectedUsername, result);
    }

    @Test
    void update() {
        Buyer existingMockUser = new Buyer("oldUser", "test", "oldEmail@gmail.com", Role.BUYER);
        existingMockUser.setId(1L);
        Buyer newMockUser = new Buyer("newUser", "test", "newEmail@gmail.com", Role.BUYER);
        newMockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingMockUser));

        String result = userService.update(newMockUser);
        assertEquals("User is updated", result);
        verify(userRepository).save(existingMockUser);
    }

    @Test
    void deleteUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        String result = userService.deleteUser(1L);
        assertEquals("User has been deleted", result);
    }
}