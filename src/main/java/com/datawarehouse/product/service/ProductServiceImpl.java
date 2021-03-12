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
import org.modelmapper.ModelMapper;
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

    private ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ArticleRepository articleRepository) {
        this.productRepository = productRepository;
        this.articleRepository = articleRepository;

        modelMapper = new ModelMapper();
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        LOGGER.info(String.format("Retrieving product: {}", id));

        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isPresent())
            return Optional.of(convertToDto(productEntityOptional.get()));
        return Optional.empty();
    }

    @Override
    public List<ProductDTO> availability() {
        LOGGER.info(String.format("Retrieving available products"));
        Iterable<ProductEntity> productEntityIterable = productRepository.findAll();
        Iterator<ProductEntity> productEntityIterator = productEntityIterable.iterator();
        
        List<ProductDTO> productDTOList = new ArrayList<>();
        ProductEntity productEntity;
        ProductDTO productDTO;
        while (productEntityIterator.hasNext()) {
            productEntity = productEntityIterator.next();
            productDTO = convertToDto(productEntity);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Transactional(rollbackFor = {ArticleNotFoundException.class,
            InsufficientStockException.class})
    @Override
    public ProductDTO sell(Long id) throws ArticleNotFoundException, InsufficientStockException, ProductNotFoundException {
        LOGGER.info(String.format("Selling product_id %s", id));

        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);

        if (!productEntityOptional.isPresent()) {
            throw new ProductNotFoundException(id);
        }

        ProductEntity productEntity = productEntityOptional.get();
        for(ProductArticleEntity productArticleEntity : productEntity.getProductArticleEntity()) {
            Optional<ArticleEntity> articleEntityOptional = articleRepository.findByArticleId(productArticleEntity.getArticleId());
            if(!articleEntityOptional.isPresent()) {
                throw new ArticleNotFoundException(productArticleEntity.getArticleId());
            }
            if(articleEntityOptional.get().getStock() == null ||
                    articleEntityOptional.get().getStock().getStock() < productArticleEntity.getAmountOf()) {
                throw new InsufficientStockException(productArticleEntity.getArticleId());
            }
            articleRepository.decreaseStock(productArticleEntity.getArticleId(), productArticleEntity.getAmountOf());
            LOGGER.info(String.format("article_id %s decreased from %s to %s", articleEntityOptional.get().getArticleId(),
                    articleEntityOptional.get().getStock().getStock().intValue(),
                    articleEntityOptional.get().getStock().getStock().intValue() - productArticleEntity.getAmountOf()));
        }

        return convertToDto(productEntity);
    }

    private ProductDTO convertToDto(ProductEntity productEntity) {
        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
        productDTO.setQuantity(getQuantity(productEntity));
        return productDTO;
    }

    /**
     * Calculates this product available quantity
     * @param productEntity the product as in database
     * @return the available quantity in stock
     */
    private Long getQuantity(ProductEntity productEntity) {
        double thisProductQuantity = -1;
        for(ProductArticleEntity productArticleEntity : productEntity.getProductArticleEntity()) {
            Optional<ArticleEntity> articleEntityOptional = articleRepository.findByArticleId(productArticleEntity.getArticleId());
            if(!articleEntityOptional.isPresent()) {
                thisProductQuantity = 0L;
                break;
            }
            if(articleEntityOptional.get().getStock() == null ||
                    articleEntityOptional.get().getStock().getStock() < productArticleEntity.getAmountOf()) {
                thisProductQuantity = 0L;
                break;
            }

            // the product availability is the min availability of each the product articles
            double thisQuantity = Math.floor(articleEntityOptional.get().getStock().getStock() /
                    productArticleEntity.getAmountOf());
            thisProductQuantity = thisProductQuantity == -1 ?
                    thisQuantity :
                    Math.min(thisProductQuantity, thisQuantity);
        }
        return Long.valueOf((long) thisProductQuantity);
    }
}
