package com.datawarehouse.product.exception;

/**
 * Product not found exception
 */
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(long id) {
        super(String.format("product_id: %s not found", id));
    }
}
