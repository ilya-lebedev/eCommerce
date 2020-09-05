package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.repositories.CartRepository;
import com.example.ecommerce.model.persistence.repositories.ItemRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import com.example.ecommerce.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    private CartController cartController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cartController = new CartController(userRepository, cartRepository, itemRepository);
    }

    @Test
    public void addToCart() {
        User user = getUser();
        Item item = getItem();

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.addToCart(getModifyCartRequest());
        Cart changedCart = responseEntity.getBody();

        verify(cartRepository, times(1)).save(any());

        assertEquals(user.getCart().getId(), changedCart.getId());
    }

    @Test
    public void removeFromCart() {
        User user = getUser();
        Item item = getItem();

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.removeFromCart(getModifyCartRequest());
        Cart changedCart = responseEntity.getBody();

        verify(cartRepository, times(1)).save(any());

        assertEquals(user.getCart().getId(), changedCart.getId());
    }

    private ModifyCartRequest getModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername("John");

        return modifyCartRequest;
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

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(1));

        return item;
    }

}
