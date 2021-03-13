package com.datawarehouse.article.service;

import com.datawarehouse.article.entity.ArticleEntity;

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
