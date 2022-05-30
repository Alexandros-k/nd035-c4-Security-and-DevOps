package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest extends TestCase {

    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"itemRepository",itemRepo);
        TestUtils.injectObjects(cartController,"userRepository",userRepo);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepo);
    }

    @Test
    public void testAddTocart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("user");
        User user = new User();
        Cart cart = new Cart();
        List<Item> itemList = new ArrayList<>();
        cart.setItems(itemList);
        user.setCart(cart);
        Item item = new Item();
        item.setId(1L);
        item.setName("box");
        item.setDescription("Heavy");
        item.setPrice(BigDecimal.valueOf(100));



        when(userRepo.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertEquals(200,cartResponseEntity.getStatusCodeValue());

    }
    @Test
    public void testRemoveFromcart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(0);
        modifyCartRequest.setUsername("user");
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Item item = new Item();
        item.setId(1L);
        item.setName("box");
        item.setDescription("Heavy");
        item.setPrice(BigDecimal.valueOf(100));
        List<Item> itemList = Arrays.asList(item);
        cart.setItems(itemList);



        when(userRepo.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
        assertEquals(200,cartResponseEntity.getStatusCodeValue());
    }
}