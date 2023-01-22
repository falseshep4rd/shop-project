package com.accenture.zonetwo.business.service;

import com.accenture.zonetwo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductById(Long id);

    List<Product> findAllProducts();

    Product saveProduct(Product product) throws Exception;

    void deleteProduct(Long id);
}
