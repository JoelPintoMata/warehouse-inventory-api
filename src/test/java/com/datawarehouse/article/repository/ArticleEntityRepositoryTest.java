package com.datawarehouse.article.repository;

import com.datawarehouse.BaseTest;
import com.datawarehouse.Main;
import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.stock.entity.StockEntity;
import com.datawarehouse.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = Main.class)
class ArticleEntityRepositoryTest extends BaseTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    public void beforeEach() {
        productRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    public void givenArticles_whenPersist_thenOk() {
        StockEntity stockEntity1 = new StockEntity();
        stockEntity1.setStock(10L);

        ArticleEntity articleEntity1 = new ArticleEntity();
        articleEntity1.setArticleId(1L);
        articleEntity1.setName("name 1");
        articleEntity1.setStock(stockEntity1);
        articleRepository.save(articleEntity1);

        ArticleEntity articleEntity2 = new ArticleEntity();
        articleEntity2.setArticleId(2L);
        articleEntity2.setName("name 2");
        articleRepository.save(articleEntity2);

        List<ArticleEntity> articleEntityList = new ArrayList<>();
        articleRepository.findAll().iterator().forEachRemaining(articleEntityList::add);

        Assertions.assertNotNull(articleEntityList);
        Assertions.assertEquals(2, articleEntityList.size());
        Assertions.assertEquals(stockEntity1.getStock(), articleEntityList.get(0).getStock().getStock());
        Assertions.assertNull(articleEntityList.get(1).getStock());
    }

    @Test
    public void givenArticles_whenDecreaseStock_thenOk() {
        StockEntity stockEntity1 = new StockEntity();
        stockEntity1.setStock(10L);

        ArticleEntity articleEntity1 = new ArticleEntity();
        articleEntity1.setArticleId(1L);
        articleEntity1.setName("name 1");
        articleEntity1.setStock(stockEntity1);
        articleRepository.save(articleEntity1);

        articleRepository.decreaseStock(articleEntity1.getArticleId(), 5L);

        List<ArticleEntity> articleEntityList = new ArrayList<>();
        articleRepository.findAll().iterator().forEachRemaining(articleEntityList::add);

        Assertions.assertNotNull(articleEntityList);
        Assertions.assertEquals(1, articleEntityList.size());
        Assertions.assertEquals("5", articleEntityList.get(0).getStock().getStock().toString());
    }
}