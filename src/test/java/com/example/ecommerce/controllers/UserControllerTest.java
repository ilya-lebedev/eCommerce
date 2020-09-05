package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserController userController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
    }

    @Test
    public void findById() {
        User user = getUser();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(getUser()));

        ResponseEntity<User> responseEntity = userController.findById(1L);
        User foundUser = responseEntity.getBody();

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getCart().getId(), foundUser.getCart().getId());
    }

    @Test
    public void findByUserName() {
        User user = getUser();

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.findByUserName("username");
        User foundUser = responseEntity.getBody();

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getCart().getId(), foundUser.getCart().getId());
    }

    @Test
    public void createUser() {
        User user = getUser();
        Cart cart = getCart();

        when(cartRepository.save(any())).thenReturn(cart);
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any())).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.createUser(getCreateUserRequest());
        User createdUser = responseEntity.getBody();

        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getPassword(), createdUser.getPassword());
        assertEquals(cart.getId(), createdUser.getCart().getId());
    }

    private CreateUserRequest getCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("John");
        request.setPassword("secret123");
        request.setConfirmPassword("secret123");

        return request;
    }

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("John");
        user.setPassword("secret123");
        user.setCart(getCart());

        return user;
    }

    private Cart getCart() {
        Cart cart = new Cart();
        cart.setId(1L);

        return cart;
    }

}
