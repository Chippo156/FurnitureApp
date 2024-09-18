package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.CategoryDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface iCategoryService {

    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long categoryId,CategoryDTO categoryDTO) throws DataNotFoundException;
    void deleteCategory(Long categoryId) throws DataNotFoundException;
    Category getCategoryById(Long categoryId);
    Page<Category> getAllCategories(String keyword, Pageable pageable);



}
