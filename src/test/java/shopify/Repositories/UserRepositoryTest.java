package shopify.Repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shopify.Data.Models.Role;
import shopify.Data.Models.User;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void findByUsernameTest() {
        User userTest = new User(
                "Bashir",
                "test",
                "ba@gmail.com",
                Role.USER
        );

        underTest.save(userTest);
        //when
        User expected = underTest.findByUsername("Bashir").get();
        assertThat(expected).isEqualTo(userTest);
    }
}