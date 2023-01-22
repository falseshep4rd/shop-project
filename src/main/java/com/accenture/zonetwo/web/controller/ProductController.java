package com.accenture.zonetwo.web.controller;

import com.accenture.zonetwo.business.service.ProductService;
import com.accenture.zonetwo.model.Product;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        log.info("Retrieve list of products");
        List<Product> productList = productService.findAllProducts();
        if(productList.isEmpty()) {
            log.warn("Product list is empty: {}", productList);
            return ResponseEntity.notFound().build();
        }
        log.info("Product list is found. Size: {}", productList::size);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@NonNull @PathVariable Long id) {
        log.info("Find product by passing product id, where id is: {}", id);
        Optional<Product> product = (productService.findProductById(id));
        if(product.isEmpty()) {
            log.warn("Product with id {} is not found", id);
        } else {
            log.info("Product with id {} is found: {}", id, product);
        }
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> saveProduct(@RequestBody Product product, BindingResult bindingResult) throws Exception {
        log.info("Create new product by passing {}", product);
        if(bindingResult.hasErrors()) {
            log.error("New product is not created: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }

        Product productSaved = productService.saveProduct(product);
        log.info("New product is created: {}", product);
        return new ResponseEntity<>(productSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Product> deleteProductById(@NonNull @PathVariable Long id) {
        log.info("Delete product by passing id, where id is: {}", id);
        Optional<Product> product = productService.findProductById(id);
        if(product.isEmpty()) {
            log.warn("Product with id: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(id);
        log.info("Product with id {} is deleted: {}", id, product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Product> updateProductById(@NonNull @PathVariable Long id,
                                                     @RequestBody Product product, BindingResult bindingResult) throws Exception {
        product.setId(id);
        log.info("Update existing product with id: {} and new body: {}", id, product);
        if (bindingResult.hasErrors() || !id.equals(product.getId())) {
            log.warn("Product with id: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        productService.saveProduct(product);
        log.info("Product with id {} is updated: {}", id, product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
}
