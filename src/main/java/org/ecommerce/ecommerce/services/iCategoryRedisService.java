package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.models.Category;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface iCategoryRedisService {
    void clear();
    List<Category> getAllCategories(String keyword, PageRequest pageRequest) throws Exception;
    void saveAllCategories(List<Category> categories,String keyword, PageRequest pageRequest) throws Exception;
}
