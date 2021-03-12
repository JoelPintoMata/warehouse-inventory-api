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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;
    private ProductDTOModelAssembler assembler;

    @Autowired
    public ProductController(ProductService productService, ProductDTOModelAssembler assembler) {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<ProductDTO>> findById(@PathVariable Long id) {
        Optional<ProductDTO> productDTOOptional = productService.findById(id);

        EntityModel<ProductDTO> entityModel = null;
        ResponseEntity<EntityModel<ProductDTO>> entityModelResponseEntity;
        if (productDTOOptional.isPresent()) {
            entityModel = assembler.toModel(productDTOOptional.get());
            entityModelResponseEntity = ResponseEntity.ok(entityModel);
        } else {
            entityModelResponseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(entityModel);
        }
        return entityModelResponseEntity;
    }

    @GetMapping("/products/availability")
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<ProductEntity>>> getAvailability() {
        List<ProductDTO> productDTOList = productService.availability();
        List<EntityModel> entityModelList = productDTOList.stream().map(productDTO -> assembler.toModel(productDTO))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CollectionModel(entityModelList,
                linkTo(methodOn(ProductController.class).getAvailability()).withSelfRel()
        ));
    }

    @DeleteMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<ProductEntity>>> sellProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTOOptional = productService.findById(id);

        if (!productDTOOptional.isPresent()) {
            EntityModel entityModel = new EntityModel(linkTo(methodOn(ProductController.class).getAvailability()).withSelfRel());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CollectionModel(Arrays.asList(entityModel),
                    linkTo(methodOn(ProductController.class).getAvailability()).withSelfRel()
            ));
        }

        EntityModel entityModel = assembler.toModel(productDTOOptional.get());
        try {
            productService.sell(id);
        } catch (InsufficientStockException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new CollectionModel(Arrays.asList(entityModel),
                    linkTo(methodOn(ProductController.class).getAvailability()).withSelfRel()
            ));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CollectionModel(Arrays.asList(entityModel),
                    linkTo(methodOn(ProductController.class).getAvailability()).withSelfRel()
            ));
        }

        return ResponseEntity.ok(new CollectionModel(Arrays.asList(entityModel),
                linkTo(methodOn(ProductController.class).findById(productDTOOptional.get().getId())).withSelfRel()
        ));
    }
}
