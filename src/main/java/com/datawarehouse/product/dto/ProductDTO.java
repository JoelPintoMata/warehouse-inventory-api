package com.datawarehouse.product.dto;

/**
 * Product DTO
 */
public class ProductDTO {

    private Long id;

    private String name;

    private Long quantity;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}