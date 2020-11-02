package com.datawarehouse.loader.inventory.loader;

import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.repository.ArticleRepository;
import com.datawarehouse.article.stock.entity.StockEntity;
import com.datawarehouse.loader.DataLoader;
import com.datawarehouse.loader.DataLoaderManager;
import com.datawarehouse.loader.exception.DataLoaderException;
import com.datawarehouse.loader.inventory.dto.LoadedInventoryArticlesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Loads {@link LoadedInventoryArticlesDTO} data and loads them into memory
 */
@Component("InventoryArticlesDataLoader")
public class InventoryArticlesDataLoaderImpl implements DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryArticlesDataLoaderImpl.class);

    // order in which this data should be loaded, consider foreign keys
    private static final Long ORDER = 1L;
    private ObjectMapper objectMapper;
    private ArticleRepository articleRepository;

    @Autowired
    public InventoryArticlesDataLoaderImpl(ObjectMapper objectMapper,
                                            DataLoaderManager dataLoaderManager,
                                            ArticleRepository articleRepository) {
        this.objectMapper = objectMapper;
        // Observer design pattern where this loader registers itself in the load manager
        dataLoaderManager.getLoaders().add(this);
        this.articleRepository = articleRepository;
    }

    @Override
    public void load() throws DataLoaderException {
        LOGGER.info("Loading data");
        try {
            LoadedInventoryArticlesDTO inventoryArticlesDTO = objectMapper.readValue(new ClassPathResource("./inventory.json").getFile(), LoadedInventoryArticlesDTO.class);
            inventoryArticlesDTO.getLoadedInventoryArticleDTOs().forEach(loadedInventoryArticleDTO -> {
                StockEntity stockEntity = new StockEntity();
                stockEntity.setStock(loadedInventoryArticleDTO.getStock());

                ArticleEntity articleEntity = new ArticleEntity();
                articleEntity.setArticleId(loadedInventoryArticleDTO.getArticleId());
                articleEntity.setName(loadedInventoryArticleDTO.getName());
                articleEntity.setStock(stockEntity);

                articleRepository.save(articleEntity);
            });
        } catch (Exception e) {
            throw new DataLoaderException(e);
        }
    }

    @Override
    public Long getOrder() {
        return ORDER;
    }
}
