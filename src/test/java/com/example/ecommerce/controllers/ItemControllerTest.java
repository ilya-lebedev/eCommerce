package com.example.ecommerce.controllers;

import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemController itemController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        itemController = new ItemController(itemRepository);
    }

    @Test
    public void getItems() {
        when(itemRepository.findAll()).thenReturn(getItemList());

        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        List<Item> items = responseEntity.getBody();

        assertEquals(2, items.size());
    }

    @Test
    public void getItemById() {
        Item item = getItem();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        Item responseItem = responseEntity.getBody();

        assertEquals(item.getId(), responseItem.getId());
    }

    @Test
    public void getItemsByName() {
        when(itemRepository.findByName(anyString())).thenReturn(getItemList());

        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("name");
        List<Item> items = responseEntity.getBody();

        assertEquals(2, items.size());
    }

    private List<Item> getItemList() {
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setId(1L);
        items.add(item);

        item = new Item();
        item.setId(2L);
        items.add(item);

        return items;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);

        return item;
    }

}
