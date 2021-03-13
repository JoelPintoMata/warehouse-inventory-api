package com.datawarehouse.product.service;

import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.dto.ProductDTO;
import com.datawarehouse.product.exception.ProductNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Product service
 */
public interface ProductService {

    Optional<ProductDTO> findById(Long id);

    /**
     * Retrieves the current product availability
     * @return a list of product and their available quantities in stock
     */
    List<ProductDTO> availability();

    /**
     * Sells a product
     * Manages article stock update
     * @param id the sold product id
     * @return the sold product
     * @throws ArticleNotFoundException     an article, making of the product, is not found
     * @throws InsufficientStockException   there are not enough articles, making of the product, to allow the selling operation to proceed
     * @throws ProductNotFoundException     the given product id is not found
     */
    ProductDTO sell(Long id) throws ArticleNotFoundException, InsufficientStockException, ProductNotFoundException;
}
