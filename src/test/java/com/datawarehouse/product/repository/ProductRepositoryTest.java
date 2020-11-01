package com.datawarehouse.product.repository;

import com.datawarehouse.BaseTest;
import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.repository.ArticleRepository;
import com.datawarehouse.product.article.entity.ProductArticleEntity;
import com.datawarehouse.product.entity.ProductEntity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
class ProductRepositoryTest extends BaseTest {

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
    public void givenProduct_whenPersist_thenPersistProductAndArticles() {
        // prepare the db
        ArticleEntity articleEntity1 = new ArticleEntity();
        articleEntity1.setArticleId(1L);
        articleRepository.save(articleEntity1);

        ArticleEntity articleEntity2 = new ArticleEntity();
        articleEntity2.setArticleId(2L);
        articleRepository.save(articleEntity2);

        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setName("name 1");

        ProductArticleEntity productArticleEntity1 = new ProductArticleEntity();
        productArticleEntity1.setArticleId(articleEntity1.getArticleId());

        ProductArticleEntity productArticleEntity2 = new ProductArticleEntity();
        productArticleEntity2.setArticleId(2L);

        productEntity1.getProductArticleEntity().add(productArticleEntity1);
        productEntity1.getProductArticleEntity().add(productArticleEntity2);

        productEntity1 = productRepository.save(productEntity1);

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setName("name 2");
        productRepository.save(productEntity2);

        List<ProductEntity> productEntityList = new ArrayList<>();
        productRepository.findAll().iterator().forEachRemaining(productEntityList::add);
        Assert.assertNotNull(productEntityList);
        Assert.assertEquals(2, productEntityList.size());

        Optional<ProductEntity> product = productRepository.findById(productEntity1.getId());
        Assert.assertNotNull(product);
        Assert.assertEquals(2, product.get().getProductArticleEntity().size());
    }
}