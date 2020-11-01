package com.datawarehouse.loader.products.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "contain_articles"
})
public class LoadedProductDTO {

    @JsonProperty("name")
    private String name;
    @JsonProperty("contain_articles")
    private List<LoadedContainArticleDTO> loadedContainArticleDTOs;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("contain_articles")
    public List<LoadedContainArticleDTO> getLoadedContainArticleDTOs() {
        return loadedContainArticleDTOs;
    }

    @JsonProperty("contain_articles")
    public void setLoadedContainArticleDTOs(List<LoadedContainArticleDTO> loadedContainArticleDTOs) {
        this.loadedContainArticleDTOs = loadedContainArticleDTOs;
    }
}