package com.accenture.zonetwo.business.service.impl;

import com.accenture.zonetwo.business.mappers.ProductMapStructMapper;
import com.accenture.zonetwo.business.repository.ProductRepository;
import com.accenture.zonetwo.business.repository.model.ProductDAO;
import com.accenture.zonetwo.business.service.ProductService;
import com.accenture.zonetwo.model.Product;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapStructMapper productMapStructMapper;

    @Override
    public Optional<Product> findProductById(Long id) {
        Optional<Product> productById = productRepository.findById(id)
                .flatMap(product -> Optional.ofNullable(productMapStructMapper.productDAOToProduct(product)));
        log.info("Product with id {} is {}", id, productById);
        return productById;
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductDAO> productDAOList = productRepository.findAll();
        log.info("Get product list. Size is: {}", productDAOList::size);
        return productDAOList.stream().map(productMapStructMapper::productDAOToProduct).collect(Collectors.toList());
    }

    @Override
    public Product saveProduct(Product product) throws Exception {
        if(!hasNoMatch(product)) {
            log.error("Product conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        ProductDAO productSaved = productRepository.save(productMapStructMapper.productToProductDAO(product));
        log.info("New product saved: {}", () -> productSaved);
        return productMapStructMapper.productDAOToProduct(productSaved);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} was deleted", id);
    }

    public boolean hasNoMatch(Product product) {
        return productRepository.findAll().stream()
                .noneMatch(t -> !t.getId().equals(product.getId()) &&
                        t.getName().equals(product.getName()) &&
                        t.getPrice().equals(product.getPrice()));
    }
}
