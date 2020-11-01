package com.datawarehouse.loader.inventory.loader;

import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.repository.ArticleRepository;
import com.datawarehouse.loader.DataLoaderManager;
import com.datawarehouse.loader.exception.DataLoaderException;
import com.datawarehouse.loader.inventory.dto.LoadedInventoryArticleDTO;
import com.datawarehouse.loader.inventory.dto.LoadedInventoryArticlesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InventoryArticlesDataLoaderImplTest {

    @InjectMocks
    private InventoryArticlesDataLoaderImpl inventoryArticlesDataLoader;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DataLoaderManager dataLoaderManager;

    @Mock
    private ArticleRepository articleRepository;

    @Test
    public void givenLoadRequest_whenExceptionIsThrow_thenExceptionRaised() throws IOException {
        doThrow(IOException.class).when(objectMapper).readValue(any(File.class), any(Class.class));
        boolean flag = false;
        try {
            inventoryArticlesDataLoader.load();
        } catch (Exception e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void givenLoadRequest_whenOk_thenLoad() throws IOException, DataLoaderException {
        LoadedInventoryArticleDTO inventoryArticleDTO = new LoadedInventoryArticleDTO();
        inventoryArticleDTO.setName("some name");
        inventoryArticleDTO.setStock(10L);
        inventoryArticleDTO.setArticleId(20L);
        List<LoadedInventoryArticleDTO> productDTOList = new ArrayList<>(1);
        productDTOList.add(inventoryArticleDTO);
        LoadedInventoryArticlesDTO loadedInventoryArticlesDTO = new LoadedInventoryArticlesDTO();
        loadedInventoryArticlesDTO.setLoadedInventoryArticleDTOs(productDTOList);
        when(objectMapper.readValue(any(File.class), any(Class.class))).thenReturn(loadedInventoryArticlesDTO);

        inventoryArticlesDataLoader.load();

        ArgumentCaptor<ArticleEntity> articleEntityArgumentCaptor = ArgumentCaptor.forClass(ArticleEntity.class);
        verify(articleRepository, timeout(1)).save(articleEntityArgumentCaptor.capture());
        assertEquals(inventoryArticleDTO.getName(), articleEntityArgumentCaptor.getValue().getName());
        assertEquals(inventoryArticleDTO.getStock(), articleEntityArgumentCaptor.getValue().getStock().getStock());
        assertEquals(inventoryArticleDTO.
                        getArticleId().longValue(),
                articleEntityArgumentCaptor.getValue().
                        getArticleId());
    }
}