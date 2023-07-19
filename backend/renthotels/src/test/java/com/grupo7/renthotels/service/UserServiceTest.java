package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.UserDTO;
import com.grupo7.renthotels.repository.FunctionRepository;
import com.grupo7.renthotels.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    FunctionRepository functionRepository;

    @InjectMocks
    UserService userService;

    @InjectMocks
    FunctionService functionService;

    @Test
    public void shouldSaveAnUser() throws NotFoundException {
        Function function = new Function(1L, 2587416L, "Admin");

        User user = new User();
        user.setFunction(function);

        UserDTO userDTO = new UserDTO(1234567L, "User", "Test", "user@test.com", "testuserpass", "Admin");

        when(functionRepository.findByName(any())).thenReturn(Optional.of(function));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        var response = userService.saveUser(userDTO);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);

    }

    @Test
    public void shouldNotSaveAnUserWithoutAFunction(){
        UserDTO userDTO = new UserDTO(1234567L, "User", "Test", "user@test.com", "testuserpass", "Admin");

        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        when(functionRepository.findByName("Admin")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.saveUser(userDTO));
    }

    @Test
    public void shouldGetAListOfUsers() {
        User user1 = new User();
        user1.setFunction(new Function());

        User user2 = new User();
        user2.setFunction(new Function());

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    public void shouldGetAnUserBySku() {
        User user = new User();
        user.setUserSku(1234567L);
        user.setFunction(new Function());

        when(userRepository.findByUserSku(1234567L)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userService.getUserBySku(1234567L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO userDTO = response.getBody();
        assertNotNull(userDTO);
    }

    @Test
    public void shouldNotGetAnInexistingUser() {
        Long sku = 1234567L;

        when(userRepository.findByUserSku(sku)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userService.getUserBySku(sku);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void shouldGetAnUserByEmail() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setFunction(new Function());

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userService.findUserByEmail("user@test.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO userDTO = response.getBody();
        assertNotNull(userDTO);
    }

    @Test
    public void shouldNotGetAnUserWithInexistingEmail() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userService.findUserByEmail(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
