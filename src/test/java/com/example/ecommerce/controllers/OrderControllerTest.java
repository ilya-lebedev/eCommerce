package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.Cart;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.User;
import com.example.ecommerce.model.persistence.UserOrder;
import com.example.ecommerce.model.persistence.repositories.OrderRepository;
import com.example.ecommerce.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    private OrderController orderController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    public void submit() {
        when(userRepository.findByUsername(anyString())).thenReturn(getUser());

        ResponseEntity<UserOrder> responseEntity = orderController.submit("username");

        assertNotNull(responseEntity.getBody());

        verify(orderRepository, times(1)).save(any());
    }

    @Test
    public void getOrdersForUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(getUser());
        when(orderRepository.findByUser(any())).thenReturn(getOrders());

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("username");

        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
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

        Item item = new Item();
        item.setId(1L);
        item.setName("some_item");
        item.setPrice(new BigDecimal(5));

        cart.addItem(item);

        return cart;
    }

    private List<UserOrder> getOrders() {
        List<UserOrder> orders = new ArrayList<>();

        UserOrder order = new UserOrder();
        order.setId(1L);
        orders.add(order);

        order = new UserOrder();
        order.setId(2L);
        orders.add(order);

        return orders;
    }

}
