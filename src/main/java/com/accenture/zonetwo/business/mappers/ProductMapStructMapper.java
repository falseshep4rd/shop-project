package com.accenture.zonetwo.business.mappers;

import com.accenture.zonetwo.business.repository.model.ProductDAO;
import com.accenture.zonetwo.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {
    ProductDAO productToProductDAO(Product product);
    Product productDAOToProduct(ProductDAO productDAO);
}
