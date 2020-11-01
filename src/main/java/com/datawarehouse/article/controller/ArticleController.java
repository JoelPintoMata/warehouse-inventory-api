package com.datawarehouse.article.controller;

import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.service.ArticleService;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.exception.ProductNotFoundException;
import com.datawarehouse.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    @ResponseBody
    public Iterable<ArticleEntity> getArticles() {
        return articleService.findAll();
    }
}