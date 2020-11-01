package com.datawarehouse.article.service;

import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.exception.ProductNotFoundException;

import java.util.Optional;

/**
 * Article service
 */
public interface ArticleService {

    /**
     * Retrieves all articles
     * @return a list of articles
     */
    Iterable<ArticleEntity> findAll();
}
