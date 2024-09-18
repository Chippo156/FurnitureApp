package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import org.ecommerce.ecommerce.services.impl.CategoryRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.LogManager;
import java.util.logging.Logger;

@Service
public class CategoryListener {
    @Autowired
    private CategoryRedisService categoryRedisService;
    private static final Logger logger = Logger.getLogger(CategoryListener.class.getName());
    @PrePersist
    public void prePersist(Category category) {
        logger.info("Category is being saved to redis");
    }
    @PreUpdate
    public void preUpdate(Category category) {
        logger.info("Category is being updated in redis");

    }
    @PreRemove
    public void preRemove(Category category) {
        logger.info("Category is being removed from redis");

    }

    @PostPersist
    public void postPersist(Category category) {
        logger.info("PostPersist");
        categoryRedisService.clear();
    }
    @PostUpdate
    public void postUpdate(Category category) {
        logger.info("PostUpdate");
        categoryRedisService.clear();
    }
    @PostRemove
    public void postRemove(Category category) {
        logger.info("PostRemove");
        categoryRedisService.clear();
    }

}
