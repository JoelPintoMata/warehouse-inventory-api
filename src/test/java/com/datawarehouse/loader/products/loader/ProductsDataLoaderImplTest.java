package com.datawarehouse.loader.products.loader;

import com.datawarehouse.Main;
import com.datawarehouse.loader.DataLoaderManager;
import com.datawarehouse.loader.exception.DataLoaderException;
import com.datawarehouse.loader.products.dto.LoadedContainArticleDTO;
import com.datawarehouse.loader.products.dto.LoadedProductDTO;
import com.datawarehouse.loader.products.dto.LoadedProductsDTO;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = Main.class)
public class ProductsDataLoaderImplTest {

    @InjectMocks
    private ProductsDataLoaderImpl productsDataLoader;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DataLoaderManager dataLoaderManager;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void givenLoadRequest_whenExceptionIsThrow_thenExceptionRaised() throws IOException {
        doThrow(IOException.class).when(objectMapper).readValue(any(File.class), any(Class.class));
        boolean flag = false;
        try {
            productsDataLoader.load();
        } catch (Exception e) {
            flag = true;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void givenLoadRequest_whenOk_thenLoad() throws IOException, DataLoaderException {
        LoadedContainArticleDTO containArticleDTO = new LoadedContainArticleDTO();
        containArticleDTO.setAmountOf(10L);
        containArticleDTO.setArticleId(1L);
        List<LoadedContainArticleDTO> containArticleDTOList = new ArrayList<>(1);
        containArticleDTOList.add(containArticleDTO);
        LoadedProductDTO productDTO = new LoadedProductDTO();
        productDTO.setName("some name");
        productDTO.setLoadedContainArticleDTOs(containArticleDTOList);
        List<LoadedProductDTO> productDTOList = new ArrayList<>(1);
        productDTOList.add(productDTO);
        LoadedProductsDTO productsDTO = new LoadedProductsDTO();
        productsDTO.setLoadedProductDTOs(productDTOList);
        when(objectMapper.readValue(any(File.class), any(Class.class))).thenReturn(productsDTO);

        productsDataLoader.load();

        ArgumentCaptor<ProductEntity> productEntityArgumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository, timeout(1)).save(productEntityArgumentCaptor.capture());
        Assertions.assertEquals(productDTO.getName(), productEntityArgumentCaptor.getValue().getName());
        Assertions.assertEquals(productDTO.getLoadedContainArticleDTOs().size(), productEntityArgumentCaptor.getValue().getProductArticleEntity().size());
        Assertions.assertEquals(productDTO.getLoadedContainArticleDTOs().get(0)
                        .getAmountOf().longValue(),
                productEntityArgumentCaptor.getValue().getProductArticleEntity().stream().collect(Collectors.toList()).get(0)
                        .getAmountOf());
    }
}