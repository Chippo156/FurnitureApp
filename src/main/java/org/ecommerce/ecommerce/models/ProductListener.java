package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import org.ecommerce.ecommerce.services.impl.ProductRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ProductListener {
    @Autowired
    private  ProductRedisService productService;
    private static final Logger logger = Logger.getLogger(ProductListener.class.getName());

    @PrePersist
    public void prePersist(Product product) {
        logger.info("Product is being saved to redis");
    }
    @PostPersist
    public void postPersist(Product product) {
        logger.info("PostPersist");
        productService.clear();
    }
    @PreUpdate
    public void preUpdate(Product product) {
        logger.info("PreUpdate");

    }
    @PostUpdate
    public void postUpdate(Product product) {
        logger.info("PostUpdate");
        productService.clear();
    }
    @PreRemove
    public void preRemove(Product product) {
        logger.info("PreRemove");
    }
    @PostRemove
    public void postRemove(Product product) {
        logger.info("PostRemove");
        productService.clear();
    }







}
