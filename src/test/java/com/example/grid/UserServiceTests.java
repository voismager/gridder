package com.example.grid;

import com.example.grid.data.payload.user.CreateUser;
import com.example.grid.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTests {
    @Autowired
    private UserService userService;

    @BeforeAll
    static void init(@Autowired UserService userService) {
        CreateUser createUser = new CreateUser();
        createUser.setUsername("admin");
        createUser.setPassword("admin");
        userService.create(createUser);
    }

    @Test
    void userShouldExistAfterCreation() {
        assertThatCode(() -> {
            assertThat(userService.authenticate("admin", "admin").getAccessToken()).isNotNull();
        }).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionOnSameUserCreation() {
        assertThrows(EntityExistsException.class, () -> {
            CreateUser createUser = new CreateUser();
            createUser.setUsername("admin");
            createUser.setPassword("admin");
            userService.create(createUser);
        });
    }

    @Test
    void userShouldNotExist() {
        assertThrows(AuthenticationException.class, () -> {
            userService.authenticate("admin0", "admin0");
        });
    }
}
