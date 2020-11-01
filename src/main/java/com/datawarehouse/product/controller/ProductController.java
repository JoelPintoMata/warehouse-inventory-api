package com.datawarehouse.product.controller;

import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.dto.ProductDTO;
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
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products/availability")
    @ResponseBody
    public Iterable<ProductDTO> getAvailability() {
        return productService.availability();
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<ProductDTO> sellProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTOOptional;
        try {
            productDTOOptional = productService.sell(id);

            if (!productDTOOptional.isPresent())
                throw new ProductNotFoundException(id);

        } catch (ArticleNotFoundException | ProductNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InsufficientStockException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok(productDTOOptional.get());
    }
}