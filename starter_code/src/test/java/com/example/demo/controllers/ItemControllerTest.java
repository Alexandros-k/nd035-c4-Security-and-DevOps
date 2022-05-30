package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest extends TestCase {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepo);
    }

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> responseItems = itemController.getItems();
        assertEquals(200,responseItems.getStatusCodeValue());
    }

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setId(1L);
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        ResponseEntity<Item> responseItem = itemController.getItemById(item.getId());
        assertEquals(200,responseItem.getStatusCodeValue());
    }

    public void testGetItemsByName() {
        Item item = new Item();
        item.setId(1L);
        item.setName("box");
        List<Item> itemList = Arrays.asList(item,item);
        when(itemRepo.findByName(item.getName())).thenReturn(itemList);
        ResponseEntity<List<Item>> responseItem = itemController.getItemsByName(item.getName());
        assertEquals(200,responseItem.getStatusCodeValue());
    }
}