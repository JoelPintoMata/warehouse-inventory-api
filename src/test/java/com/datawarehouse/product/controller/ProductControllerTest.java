package com.datawarehouse.product.controller;

import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.dto.ProductDTO;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.service.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void givenProducts_whenGetAvailability_thenRetrieveProducts() throws Exception {
        List<ProductDTO> productDTOList = new ArrayList<>();
        ProductDTO productDTO1 = new ProductDTO.ProductDTOBuilder().
                setName("name 1").
                build();
        productDTOList.add(productDTO1);
        ProductDTO productDTO2 = new ProductDTO.ProductDTOBuilder().
                setName("name 2").
                build();
        productDTOList.add(productDTO2);

        given(productService.availability())
                .willReturn(productDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get("/products/availability")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productDTOList.size())));
    }

    @Test
    public void givenProductId_whenSellProduct_thenOk() throws
            Exception {
        List<ProductDTO> productEntityList = new ArrayList<>();
        ProductDTO productDTO = new ProductDTO.ProductDTOBuilder().
                setId(999L).
                setName("name 1").
                build();
        productEntityList.add(productDTO);

        given(productService.sell(999L))
                .willReturn(java.util.Optional.of(productDTO));
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(productDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(productDTO.getName())));
    }

    @Test
    public void givenProductId_whenSellProductAndNoArticleStock_thenUnprocessableEntity() throws
            Exception {
        List<ProductEntity> productEntityList = new ArrayList<>();
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setId(999L);
        productEntity1.setName("name 1");
        productEntityList.add(productEntity1);

        given(productService.sell(999L))
                .willThrow(InsufficientStockException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productEntity1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void givenProductId_whenSellProductAndNoArticle_thenNotFound() throws
            Exception {
        List<ProductEntity> productEntityList = new ArrayList<>();
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setId(999L);
        productEntity1.setName("name 1");
        productEntityList.add(productEntity1);

        given(productService.sell(999L))
                .willThrow(ArticleNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productEntity1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUnexistingProductId_whenSellProduct_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
