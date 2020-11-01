package com.datawarehouse.product.entity;

import com.datawarehouse.product.article.entity.ProductArticleEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Product entity
 * Aggregates product and {@link ProductArticleEntity}
 */
@Table("product")
public class ProductEntity {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "prod_id")
    private Set<ProductArticleEntity> productArticle = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<ProductArticleEntity> getProductArticleEntity() {
        return this.productArticle;
    }

    public void setProductArticleEntity(final Set<ProductArticleEntity> productArticle) {
        this.productArticle = productArticle;
    }
}