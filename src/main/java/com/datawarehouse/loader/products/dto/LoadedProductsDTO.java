package com.datawarehouse.loader.products.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "products"
})
public class LoadedProductsDTO {

    @JsonProperty("products")
    private List<LoadedProductDTO> loadedProductDTOs;

    @JsonProperty("products")
    public List<LoadedProductDTO> getLoadedProductDTOs() {
        return loadedProductDTOs;
    }

    @JsonProperty("products")
    public void setLoadedProductDTOs(List<LoadedProductDTO> loadedProductDTOs) {
        this.loadedProductDTOs = loadedProductDTOs;
    }
}