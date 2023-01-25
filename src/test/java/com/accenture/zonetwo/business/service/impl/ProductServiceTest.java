package com.accenture.zonetwo.business.service.impl;

import com.accenture.zonetwo.business.mappers.ProductMapStructMapper;
import com.accenture.zonetwo.business.repository.ProductRepository;
import com.accenture.zonetwo.business.repository.model.ProductDAO;
import com.accenture.zonetwo.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;
    @InjectMocks
    private ProductServiceImpl service;
    @Mock
    private ProductMapStructMapper mapper;

    private Product product;
    private ProductDAO productDAO;
    private List<Product> productList;
    private List<ProductDAO> productDAOList;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void init() {
        product = createProduct(1L, "Box", 100D);
        productDAO = createProductDAO(1L, "Box", 100D);
        productList = createProductList(product);
        productDAOList = createProductDAOList(productDAO);
    }

    @Test
    void testFindAllProducts() {
        when(repository.findAll()).thenReturn(productDAOList);
        when(mapper.productDAOToProduct(productDAO)).thenReturn(product);
        List<Product> products = service.findAllProducts();
        assertEquals(3, products.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindAllProductsInvalid() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.findAllProducts().isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(productDAO));
        when(mapper.productDAOToProduct(productDAO)).thenReturn(product);
        Optional<Product> returnedProduct = service.findProductById(product.getId());
        assertEquals(product.getId(), returnedProduct.get().getId());
        assertEquals(product.getName(), returnedProduct.get().getName());
        assertEquals(product.getPrice(), returnedProduct.get().getPrice());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void testFindProductByIdInvalid() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(service.findProductById(anyLong()).isPresent());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveProduct() throws Exception {
        when(repository.save(productDAO)).thenReturn(productDAO);
        when(mapper.productDAOToProduct(productDAO)).thenReturn(product);
        when(mapper.productToProductDAO(product)).thenReturn(productDAO);
        Product savedProduct = service.saveProduct(product);
        assertTrue(service.hasNoMatch(savedProduct));
        assertEquals(product, savedProduct);
        verify(repository, times(1)).save(productDAO);
    }

    @Test
    void testSaveProductInvalid() {
        when(repository.save(productDAO)).thenThrow(new IllegalArgumentException());
        when(mapper.productToProductDAO(product)).thenReturn(productDAO);
        assertThrows(IllegalArgumentException.class, () -> service.saveProduct(product));
        verify(repository, times(1)).save(productDAO);
    }

    @Test
    void testSaveProductInvalid_Duplicate() {
        Product productToSave = createProduct(null, "Box", 100D);
        when(repository.findAll()).thenReturn(productDAOList);
        assertThrows(HttpClientErrorException.class, () -> service.saveProduct(productToSave));
        verify(repository, times(0)). save(productDAO);
    }

    @Test
    void testDeleteProduct() {
        service.deleteProduct(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteProductInvalid() {
        doThrow(new IllegalArgumentException()).when(repository).deleteById(anyLong());
        assertThrows(IllegalArgumentException.class, () -> service.deleteProduct(anyLong()));
    }

    private List<ProductDAO> createProductDAOList(ProductDAO productDAO) {
        List<ProductDAO> productDAOList = new ArrayList<>();
        productDAOList.add(productDAO);
        productDAOList.add(productDAO);
        productDAOList.add(productDAO);
        return productDAOList;
    }

    private List<Product> createProductList(Product product) {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        productList.add(product);
        return productList;
    }

    private ProductDAO createProductDAO(Long id, String name, double price) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.setId(id);
        productDAO.setName(name);
        productDAO.setPrice(price);
        return productDAO;
    }

    private Product createProduct(Long id, String name, double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
