package com.datawarehouse.product.controller;

import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.dto.ProductDTO;
import com.datawarehouse.product.service.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @SpyBean
    private ProductDTOModelAssembler productDTOModelAssembler;

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

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("name 1");
        productDTOList.add(productDTO);

        productDTO = new ProductDTO();
        productDTO.setName("name 2");
        productDTOList.add(productDTO);

        given(productService.availability())
                .willReturn(productDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/availability")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDTOList", hasSize(productDTOList.size())));
    }

    @Test
    public void givenProductId_whenSellProduct_thenOk() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(999L);
        productDTO.setName("name 1");
        given(productService.findById(999L))
                .willReturn(Optional.of(productDTO));

        given(productService.sell(999L))
                .willReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDTOList[0].id", Matchers.equalTo(productDTO.getId().intValue())))
                .andExpect(jsonPath("$._embedded.productDTOList[0].name", Matchers.equalTo(productDTO.getName())));
    }

    @Test
    public void givenProductId_whenSellProductAndNoArticleStock_thenProcessableEntity() throws
            Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(999L);
        productDTO.setName("name 1");
        given(productService.findById(999L))
                .willReturn(Optional.of(productDTO));

        given(productService.sell(999L))
                .willThrow(InsufficientStockException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void givenProductId_whenSellProductAndNoArticle_thenNotFound() throws
            Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(999L);
        productDTO.setName("name 1");
        given(productService.sell(999L))
                .willThrow(ArticleNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUnExistingProductId_whenSellProduct_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
