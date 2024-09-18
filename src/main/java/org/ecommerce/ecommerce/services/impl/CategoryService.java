package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.dtos.CategoryDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Category;
import org.ecommerce.ecommerce.repository.CategoryRepository;
import org.ecommerce.ecommerce.services.iCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements iCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Page<Category> getAllCategories(String keyword, Pageable pageable) {
        return categoryRepository.getAllCategories(keyword, pageable);
    }


    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        try {
            new Category();
            Category category = Category
                    .builder()
                    .name(categoryDTO.getName())
                    .build();
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new RuntimeException("Category not created");
        }
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws DataNotFoundException {
        try {
            Category existCategory = getCategoryById(categoryId);
            existCategory.setName(categoryDTO.getName());
            return categoryRepository.save(existCategory);
        } catch (Exception e) {
            throw new DataNotFoundException("Category not updated");
        }
    }

    @Override
    public void deleteCategory(Long categoryId) throws DataNotFoundException {
        try {
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            throw new DataNotFoundException("Category not deleted");
        }
    }


}
