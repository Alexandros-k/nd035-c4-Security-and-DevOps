package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import junit.framework.TestCase;
import org.h2.command.dml.MergeUsing;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.criteria.Order;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest extends TestCase {

    private OrderController orderController;
    
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepo);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepo);
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
    }

    @Test
    public void testSubmit() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        Item item = new Item();

        final ResponseEntity<User> response = userController.createUser(r);
        User u = response.getBody();
        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        UserOrder order= new UserOrder();
        order.setItems(Arrays.asList(item,item));
        order.setTotal(BigDecimal.valueOf(100));
        order.setUser(u);
        u.getCart().setId(1L);
        u.getCart().setItems(Arrays.asList(item,item));
        u.getCart().setTotal(BigDecimal.valueOf(100));
        u.getCart().setUser(u);
        final ResponseEntity<UserOrder> responseOrd = orderController.submit(u.getUsername());
        assertEquals(200,responseOrd.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        User u = response.getBody();
        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<List<UserOrder>> responseOrd = orderController.getOrdersForUser(u.getUsername());
        assertEquals(200,responseOrd.getStatusCodeValue());
    }
}