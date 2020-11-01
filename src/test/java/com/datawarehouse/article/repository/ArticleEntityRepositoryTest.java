package com.datawarehouse.article.repository;

import com.datawarehouse.BaseTest;
import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.stock.entity.StockEntity;
import com.datawarehouse.product.repository.ProductRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
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

        Assert.assertNotNull(articleEntityList);
        Assert.assertEquals(2, articleEntityList.size());
        Assert.assertEquals(stockEntity1.getStock(), articleEntityList.get(0).getStock().getStock());
        Assert.assertNull(articleEntityList.get(1).getStock());
    }

    @Test
    public void givenArticles_whenDecreseStocl_thenOk() {
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

        Assert.assertNotNull(articleEntityList);
        Assert.assertEquals(1, articleEntityList.size());
        Assert.assertEquals("5", articleEntityList.get(0).getStock().getStock().toString());
    }
}