package com.datawarehouse.product.service;

import com.datawarehouse.article.entity.ArticleEntity;
import com.datawarehouse.article.exception.ArticleNotFoundException;
import com.datawarehouse.article.repository.ArticleRepository;
import com.datawarehouse.article.stock.exception.InsufficientStockException;
import com.datawarehouse.product.article.entity.ProductArticleEntity;
import com.datawarehouse.product.dto.ProductDTO;
import com.datawarehouse.product.entity.ProductEntity;
import com.datawarehouse.product.exception.ProductNotFoundException;
import com.datawarehouse.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Product service implementation
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ArticleRepository articleRepository) {
        this.productRepository = productRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Iterable<ProductDTO> availability() {
        List<ProductDTO> result = new ArrayList<>();
        LOGGER.info(String.format("Retrieving available products"));

        Iterable<ProductEntity> productEntityIterable = productRepository.findAll();

        Iterator<ProductEntity> iterator = productEntityIterable.iterator();
        while(iterator.hasNext()) {
            ProductEntity productEntity = iterator.next();
            long quantity = 0L;
            try {
                quantity = getQuantity(productEntity);
            } catch (Exception e) {
                LOGGER.warn(String.format("Could not calculate product_id %s availability", productEntity.getId()), e);
            }
            result.add(new ProductDTO.ProductDTOBuilder().
                    setId(productEntity.getId()).
                    setName(productEntity.getName()).
                    setQuantity(quantity).
                    build()
            );
        }
        return result;
    }

    @Transactional(rollbackFor = {ArticleNotFoundException.class,
            InsufficientStockException.class})
    @Override
    public Optional<ProductDTO> sell(Long id) throws ArticleNotFoundException, InsufficientStockException, ProductNotFoundException {
        LOGGER.info(String.format("Selling product_id %s", id));

        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);

        if (!productEntityOptional.isPresent()) {
            throw new ProductNotFoundException(id);
        }

        return Optional.of(new ProductDTO.ProductDTOBuilder().
                setId(productEntityOptional.get().getId()).
                setName(productEntityOptional.get().getName()).
                setQuantity(getQuantity(productEntityOptional.get())).
                build()
        );
    }

    /**
     * Calculates this product available quantity
     * @param productEntity the product as in database
     * @return the available quantity in stock
     * @throws ArticleNotFoundException
     * @throws InsufficientStockException
     */
    private Long getQuantity(ProductEntity productEntity) throws ArticleNotFoundException, InsufficientStockException {
        double thisProductQuantity = -1;
        for(ProductArticleEntity productArticleEntity : productEntity.getProductArticleEntity()) {
            Optional<ArticleEntity> articleEntityOptional = articleRepository.findByArticleId(productArticleEntity.getArticleId());
            if(!articleEntityOptional.isPresent()) {
                throw new ArticleNotFoundException(productArticleEntity.getArticleId());
            }
            if(articleEntityOptional.get().getStock() == null ||
                    articleEntityOptional.get().getStock().getStock() < productArticleEntity.getAmountOf()) {
                throw new InsufficientStockException(productArticleEntity.getArticleId());
            }

            // the product availability is the min availability of each the product articles
            double thisQuantity = Math.floor(articleEntityOptional.get().getStock().getStock() /
                    productArticleEntity.getAmountOf());
            if(thisProductQuantity == -1) {
                thisProductQuantity = thisQuantity;
            } else {
                thisProductQuantity = Math.min(thisProductQuantity, thisQuantity);
            }
            articleRepository.decreaseStock(productArticleEntity.getArticleId(), productArticleEntity.getAmountOf());
            LOGGER.info(String.format("article_id %s decreased from %s to %s", articleEntityOptional.get().getArticleId(),
                    articleEntityOptional.get().getStock().getStock().intValue(),
                    articleEntityOptional.get().getStock().getStock().intValue() - productArticleEntity.getAmountOf()));
        }
        return Long.valueOf((long) thisProductQuantity);
    }
}
