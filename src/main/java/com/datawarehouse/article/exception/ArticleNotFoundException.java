package com.datawarehouse.article.exception;

/**
 * Article not found exception
 */
public class ArticleNotFoundException extends Exception {

    public ArticleNotFoundException(long id) {
        super(String.format("article_id: %s not found",id));
    }
}
