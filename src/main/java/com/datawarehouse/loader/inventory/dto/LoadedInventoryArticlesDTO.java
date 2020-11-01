package com.datawarehouse.loader.inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "inventory"
})
public class LoadedInventoryArticlesDTO {

    @JsonProperty("inventory")
    private List<LoadedInventoryArticleDTO> loadedInventoryArticleDTOs;

    @JsonProperty("inventory")
    public List<LoadedInventoryArticleDTO> getLoadedInventoryArticleDTOs() {
        return loadedInventoryArticleDTOs;
    }

    @JsonProperty("inventory")
    public void setLoadedInventoryArticleDTOs(List<LoadedInventoryArticleDTO> loadedInventoryArticleDTOs) {
        this.loadedInventoryArticleDTOs = loadedInventoryArticleDTOs;
    }
}