package com.accenture.zonetwo.web.controller;

import com.accenture.zonetwo.business.service.ProductService;
import com.accenture.zonetwo.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private String URL = "/product";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController controller;

    @MockBean
    private ProductService service;

    @Test
    void testFindAllProducts() throws Exception {
        List<Product> productList = createProductList();

        when(service.findAllProducts()).thenReturn(productList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Box"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(100D))
                .andExpect(status().isOk());
        verify(service, times(1)).findAllProducts();
    }

    @Test
    void testFindAllProductsInvalid() throws Exception {
        List<Product> productList = createProductList();
        productList.clear();

        when(service.findAllProducts()).thenReturn(productList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .content(asJsonString(productList))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findAllProducts();
    }

    @Test
    void testFindProductById() throws Exception {
        Optional<Product> product = Optional.of(createProduct());

        when(service.findProductById(anyLong())).thenReturn(product);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Box"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100D))
                .andExpect(status().isOk());

        verify(service, times(1)).findProductById(anyLong());
    }

    @Test
    void testFindProductByIdInvalid() throws Exception {
        Optional<Product> product = Optional.of(createProduct());
        product.get().setId(null);

        when(service.findProductById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findProductById(null);
    }

    @Test
    void testSaveProduct() throws Exception {
        Product product = createProduct();
        product.setId(null);

        when(service.saveProduct(product)).thenReturn(product);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveProduct(product);
    }

    @Test
    void testSaveProductInvalid() throws Exception {
        Product product = createProduct();
        product.setName("");

        when(service.saveProduct(product)).thenReturn(product);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).saveProduct(product);
    }

    @Test
    void testUpdateProductById() throws Exception {
        Product product = createProduct();

        when(service.findProductById(product.getId())).thenReturn(Optional.of(product));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveProduct(product);
    }

    @Test
    void testUpdateProductByIdInvalid() throws Exception {
        Product product = createProduct();
        product.setId(null);

        when(service.findProductById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).saveProduct(product);
    }

    @Test
    void testDeleteProduct() throws Exception {
        Optional<Product> product = Optional.of(createProduct());

        when(service.findProductById(anyLong())).thenReturn(product);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteProduct(anyLong());
    }

    @Test
    void testDeleteProductInvalid() throws Exception {
        Optional<Product> product = Optional.of(createProduct());
        product.get().setId(null);

        when(service.findProductById(null)).thenReturn(product);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteProduct(anyLong());
    }

    private Product createProduct() {
        return new Product(1L, "Box", 100D);
    }

    private List<Product> createProductList() {
        Product productOne = createProduct();
        Product productTwo = createProduct();
        List<Product> productList = new ArrayList<>();
        productList.add(productOne);
        productList.add(productTwo);
        return productList;
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
