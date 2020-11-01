package com.datawarehouse.article.entity;

import com.datawarehouse.article.stock.entity.StockEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Article entity
 */
@Table("article")
public class ArticleEntity {

    @Id
    private long id;

    @Column("art_id")
    private long articleId;

    private String name;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private StockEntity stock;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticleId() {
        return this.articleId;
    }

    public void setArticleId(long artId) {
        this.articleId = artId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockEntity getStock() {
        return this.stock;
    }

    public void setStock(StockEntity stock) {
        this.stock = stock;
    }
}