package com.datawarehouse.article.repository;

import com.datawarehouse.article.entity.ArticleEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link ArticleEntity} repository
 */
@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {

    @Query("select * from article where art_id = :articleId")
    Optional<ArticleEntity> findByArticleId(long articleId);

    @Modifying
    @Query("update article set stock = stock - :amountOf where art_id = :articleId")
    void decreaseStock(long articleId, long amountOf);
}