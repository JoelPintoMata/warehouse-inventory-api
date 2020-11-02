package com.datawarehouse.product.dto;

/**
 * Product DTO
 */
public class ProductDTO {

    private Long id;

    private String name;

    private Long quantity;

    private ProductDTO(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

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

    /**
     * Product DTO builder
     */
    public static class ProductDTOBuilder {

        private Long id;

        private String name;

        private Long quantity;

        public ProductDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ProductDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductDTOBuilder setQuantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(id, name, quantity);
        }
    }
}
