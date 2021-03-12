package com.datawarehouse.product.dto;

import org.springframework.hateoas.RepresentationModel;

/**
 * Product DTO
 */
public class ProductDTO extends RepresentationModel<ProductDTO> {

    private Long id;

    private String name;

    private Long quantity;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
