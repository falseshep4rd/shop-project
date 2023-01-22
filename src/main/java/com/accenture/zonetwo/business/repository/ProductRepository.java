package com.accenture.zonetwo.business.repository;

import com.accenture.zonetwo.business.repository.model.ProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDAO, Long> {
}
