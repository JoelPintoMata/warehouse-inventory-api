package com.datawarehouse.product.article.entity;

import com.datawarehouse.product.entity.ProductEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * ProductArticle entity
 * Aggregated by {@link ProductEntity}
 */
@Table("product_article")
public class ProductArticleEntity {

    @Id
    private Long id;
    @Column("prod_id")
    private long productId;
    @Column("art_id")
    private long articleId;
    @Column("amount_of")
    private long amountOf;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(final long productId) {
        this.productId = productId;
    }

    public long getArticleId() {
        return this.articleId;
    }

    public void setArticleId(final long articleId) {
        this.articleId = articleId;
    }

    public long getAmountOf() {
        return this.amountOf;
    }

    public void setAmountOf(final long amountOf) {
        this.amountOf = amountOf;
    }

}
