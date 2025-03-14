package shopify.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;
import shopify.Repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    UserService underTest;
    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @Test
    void getAll() {
        underTest.getAll();

        verify(userRepository).findAll();
    }
    User user = new User("Josh",
            "kong",
            "Lane@jj.com",
            Role.USER);

    @Test
    @Disabled
    void authenticatedUsername() {
    }

    @Test
    @Disabled
    void update() {
        underTest.update(user);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
    }

    @Test
    @Disabled
    void deleteUser() {
    }
}