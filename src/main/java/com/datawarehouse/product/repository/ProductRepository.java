package com.datawarehouse.product.repository;

import com.datawarehouse.product.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Product repository
 */
@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}