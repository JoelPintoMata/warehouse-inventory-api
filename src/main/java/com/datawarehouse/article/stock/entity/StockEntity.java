package com.datawarehouse.article.stock.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 * {@link StockEntity} entity
 */
@Table("stock")
public class StockEntity {

    private Long stock;

    public Long getStock() {
        return this.stock;
    }

    public void setStock(final Long stock) {
        this.stock = stock;
    }
}