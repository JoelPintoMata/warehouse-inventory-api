package com.datawarehouse.loader.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "art_id",
        "name",
        "stock"
})
public class LoadedInventoryArticleDTO {

    @JsonProperty("art_id")
    private Long articleId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("stock")
    private Long stock;

    @JsonProperty("art_id")
    public Long getArticleId() {
        return articleId;
    }

    @JsonProperty("art_id")
    public void setArticleId(Long artId) {
        this.articleId = artId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("stock")
    public Long getStock() {
        return stock;
    }

    @JsonProperty("stock")
    public void setStock(Long stock) {
        this.stock = stock;
    }
}