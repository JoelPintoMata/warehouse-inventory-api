package com.datawarehouse.loader.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "art_id",
        "amount_of"
})
public class LoadedContainArticleDTO {

    @JsonProperty("art_id")
    private Long articleId;

    @JsonProperty("amount_of")
    private Long amountOf;

    @JsonProperty("art_id")
    public Long getArticleId() {
        return articleId;
    }

    @JsonProperty("art_id")
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @JsonProperty("amount_of")
    public Long getAmountOf() {
        return amountOf;
    }

    @JsonProperty("amount_of")
    public void setAmountOf(Long amountOf) {
        this.amountOf = amountOf;
    }
}