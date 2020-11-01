package com.datawarehouse.loader.products.loader;

import com.datawarehouse.loader.DataLoader;
import com.datawarehouse.loader.DataLoaderManager;
import com.datawarehouse.loader.exception.DataLoaderException;
import com.datawarehouse.loader.products.dto.LoadedProductsDTO;
import com.datawarehouse.product.article.entity.ProductArticleEntity;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads {@link LoadedProductsDTO} data and loads them into memory
 */
@Component("ProductsDataLoader")
public class ProductsDataLoaderImpl implements DataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsDataLoaderImpl.class);

    // order in which this data should be loaded, consider foreign keys
    private static final Long ORDER = 2L;
    private ObjectMapper objectMapper;
    private ProductRepository productRepository;

    @Autowired
    public ProductsDataLoaderImpl(ObjectMapper objectMapper,
                                   DataLoaderManager dataLoaderManager,
                                   ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        dataLoaderManager.getLoaders().add(this);
        this.productRepository = productRepository;
    }

    @Override
    public void load() throws DataLoaderException {
        LOGGER.info("Loading data");
        try {
            LoadedProductsDTO productsDTO = objectMapper.readValue(new ClassPathResource("./products.json").getFile(), LoadedProductsDTO.class);
            productsDTO.getLoadedProductDTOs().forEach(loadedProductDTO -> {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setName(loadedProductDTO.getName());

                List<ProductArticleEntity> productArticleEntityList = new ArrayList<>();
                loadedProductDTO.getLoadedContainArticleDTOs().forEach(loadedProductArticleDTO -> {
                    ProductArticleEntity productArticleEntity = new ProductArticleEntity();
                    productArticleEntity.setArticleId(loadedProductArticleDTO.getArticleId());
                    productArticleEntity.setAmountOf(loadedProductArticleDTO.getAmountOf());
                    productArticleEntityList.add(productArticleEntity);
                });
                productEntity.getProductArticleEntity().addAll(productArticleEntityList);
                productRepository.save(productEntity);
            });
        } catch (IOException e) {
            throw new DataLoaderException(e);
        }
    }

    @Override
    public Long getOrder() {
        return ORDER;
    }
}