package com.datawarehouse.product.service;

import com.datawarehouse.Main;
import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.repository.ArticleRepository;
import com.datawarehouse.article.stock.entity.StockEntity;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.article.entity.ProductArticleEntity;
import com.datawarehouse.product.dto.ProductDTO;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.exception.ProductNotFoundException;
import com.datawarehouse.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Main.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenProducts_whenRequestAvailability_thenOk() {
        StockEntity stockEntity1 = new StockEntity();
        stockEntity1.setStock(20L);
        ArticleEntity articleEntity1 = new ArticleEntity();
        articleEntity1.setId(1L);
        articleEntity1.setArticleId(1L);
        articleEntity1.setStock(stockEntity1);
        when(articleRepository.findByArticleId(articleEntity1.getId())).thenReturn(Optional.of(articleEntity1));

        StockEntity stockEntity2 = new StockEntity();
        stockEntity2.setStock(7L);
        ArticleEntity articleEntity2 = new ArticleEntity();
        articleEntity2.setId(2L);
        articleEntity2.setArticleId(2L);
        articleEntity2.setStock(stockEntity2);
        when(articleRepository.findByArticleId(articleEntity2.getId())).thenReturn(Optional.of(articleEntity2));

        StockEntity stockEntity3 = new StockEntity();
        stockEntity3.setStock(30L);
        ArticleEntity articleEntity3 = new ArticleEntity();
        articleEntity3.setId(3L);
        articleEntity3.setArticleId(3L);
        articleEntity3.setStock(stockEntity3);
        when(articleRepository.findByArticleId(articleEntity3.getId())).thenReturn(Optional.of(articleEntity3));

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);

        ProductArticleEntity productArticleEntity1 = new ProductArticleEntity();
        productArticleEntity1.setId(1L);
        productArticleEntity1.setArticleId(articleEntity1.getArticleId());
        productArticleEntity1.setAmountOf(4L);
        productEntity.getProductArticleEntity().add(productArticleEntity1);

        ProductArticleEntity productArticleEntity2 = new ProductArticleEntity();
        productArticleEntity2.setId(1L);
        productArticleEntity2.setArticleId(articleEntity2.getArticleId());
        productArticleEntity2.setAmountOf(2L);
        productEntity.getProductArticleEntity().add(productArticleEntity2);

        ProductArticleEntity productArticleEntity3 = new ProductArticleEntity();
        productArticleEntity3.setId(1L);
        productArticleEntity3.setArticleId(articleEntity3.getArticleId());
        productArticleEntity3.setAmountOf(3L);
        productEntity.getProductArticleEntity().add(productArticleEntity3);

        List<ProductEntity> productEntityList = new ArrayList<>();
        productEntityList.add(productEntity);
        when(productRepository.findAll()).thenReturn(productEntityList);

        List<ProductDTO> productDTOList = productService.availability();

        Assertions.assertEquals(productEntityList.size(), productDTOList.size());
        Assertions.assertEquals(3, productDTOList.get(0).getQuantity().intValue());
    }

    @Test
    public void givenProductWithUnavailableArticle_whenRequestAvailability_thenOk() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);

        ProductArticleEntity productArticleEntity1 = new ProductArticleEntity();
        productArticleEntity1.setId(1L);
        productArticleEntity1.setArticleId(1L);
        productArticleEntity1.setAmountOf(4L);
        productEntity.getProductArticleEntity().add(productArticleEntity1);

        List<ProductEntity> productEntityList = new ArrayList<>();
        productEntityList.add(productEntity);
        when(productRepository.findAll()).thenReturn(productEntityList);

        List<ProductDTO> productDTOList = productService.availability();

        Assertions.assertEquals(productEntityList.size(), productDTOList.size());
        Assertions.assertEquals(0, productDTOList.get(0).getQuantity().intValue());
    }

    @Test
    public void givenNoProductInDatabase_wheSell_thenNotFound() {
        assertThrows(ProductNotFoundException.class, () ->
                productService.sell(1L));
    }

    @Test
    public void givenNoArticleInDatabase_whenSell_thenNotFound() throws ProductNotFoundException, InsufficientStockException {
        ProductArticleEntity productArticleEntity = new ProductArticleEntity();
        productArticleEntity.setId(1L);
        productArticleEntity.setArticleId(1L);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.getProductArticleEntity().add(productArticleEntity);
        when(productRepository.findById(any())).thenReturn(Optional.of(productEntity));

        assertThrows(ArticleNotFoundException.class, () ->
                productService.sell(1L));
    }

    @Test
    public void givenArticleWithNullStockInDatabase_whenSell_thenException() throws ProductNotFoundException, ArticleNotFoundException, InsufficientStockException {
        ProductArticleEntity productArticleEntity = new ProductArticleEntity();
        productArticleEntity.setId(1L);
        productArticleEntity.setArticleId(1L);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.getProductArticleEntity().add(productArticleEntity);
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(1L);
        when(articleRepository.findByArticleId(productArticleEntity.getArticleId())).thenReturn(Optional.of(articleEntity));

        assertThrows(InsufficientStockException.class, () ->
                productService.sell(1L));
    }

    @Test
    public void givenArticleWithoutStockInDatabase_whenSell_thenException() throws ProductNotFoundException, ArticleNotFoundException, InsufficientStockException {
        ProductArticleEntity productArticleEntity = new ProductArticleEntity();
        productArticleEntity.setId(1L);
        productArticleEntity.setArticleId(1L);
        productArticleEntity.setAmountOf(2L);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.getProductArticleEntity().add(productArticleEntity);
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        StockEntity stockEntity = new StockEntity();
        stockEntity.setStock(3L);

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(productArticleEntity.getArticleId());
        articleEntity.setStock(stockEntity);
        when(articleRepository.findByArticleId(articleEntity.getId())).thenReturn(Optional.of(articleEntity));

        productService.sell(1L);

        stockEntity = new StockEntity();
        stockEntity.setStock(1L);

        articleEntity = new ArticleEntity();
        articleEntity.setId(productArticleEntity.getArticleId());
        articleEntity.setStock(stockEntity);
        when(articleRepository.findByArticleId(articleEntity.getId())).thenReturn(Optional.of(articleEntity));

        assertThrows(InsufficientStockException.class, () ->
                productService.sell(1L));
    }

    @Test
    public void givenArticleWithStockInDatabase_whenSell_thenException() throws ProductNotFoundException, ArticleNotFoundException {
        ProductArticleEntity productArticleEntity = new ProductArticleEntity();
        productArticleEntity.setId(1L);
        productArticleEntity.setProductId(1L);
        productArticleEntity.setArticleId(1L);
        productArticleEntity.setAmountOf(2L);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.getProductArticleEntity().add(productArticleEntity);
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        StockEntity stockEntity = new StockEntity();
        stockEntity.setStock(11L);

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(productArticleEntity.getArticleId());
        articleEntity.setStock(stockEntity);
        when(articleRepository.findByArticleId(productArticleEntity.getArticleId())).thenReturn(Optional.of(articleEntity));

        boolean flag = false;
        ProductDTO productDTO = null;
        try {
            productDTO = productService.sell(1L);
        } catch (Exception e) {
            flag = true;
        }
        Assertions.assertFalse(flag);
        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals(productEntity.getId(), productDTO.getId());
        Assertions.assertEquals(5, productDTO.getQuantity().intValue());
    }
}
