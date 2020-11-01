package com.datawarehouse.article.stock.exception;

/**
 * Insufficient stock exception
 */
public class InsufficientStockException extends Exception {

    public InsufficientStockException(long id) {
        super(String.format("Insufficient stock for article_id: %s",id));
    }
}
