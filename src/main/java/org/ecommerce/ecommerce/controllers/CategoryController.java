package org.ecommerce.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.CategoryDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Category;
import org.ecommerce.ecommerce.responses.CategoryListResponse;
import org.ecommerce.ecommerce.responses.DeleteResponse;
import org.ecommerce.ecommerce.responses.UpdateResponse;
import org.ecommerce.ecommerce.responses.create.CreateCategoryRepsonse;
import org.ecommerce.ecommerce.services.impl.CategoryRedisService;
import org.ecommerce.ecommerce.services.impl.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    private final CategoryRedisService categoryRedisService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getField).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Category category = categoryService.createCategory(categoryDTO);
        System.out.println(category.toString());

        try {

            logger.info("Message sent to Kafka topic: {}", "insert-a-category");
        } catch (Exception e) {
            logger.error("Error sending message to Kafka", e);
            return ResponseEntity.status(500).body("Error sending message to Kafka");
        }

        return ResponseEntity.ok(CreateCategoryRepsonse.builder()
                .category(category)
                .message("Category created successfully").build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCategories(
            @RequestParam String keyword,
            @RequestParam int page, @RequestParam int limit) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
            List<Category> categories = categoryRedisService.getAllCategories(keyword, pageRequest);
            int totalPage = 0;
            if(categories == null){
                Page<Category> categoriesPage = categoryService.getAllCategories(keyword, pageRequest);
                totalPage = categoriesPage.getTotalPages();
                categories = categoriesPage.getContent();
                categoryRedisService.saveAllCategories(categories,keyword,pageRequest);
            }
            if(categories != null) {
                totalPage = categories.size();
            }
            return ResponseEntity.ok(CategoryListResponse.builder()
                    .categories(categories)
                    .totalPage(totalPage)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getField).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(UpdateResponse.builder().message("Category updated successfully").build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteCategory(@PathVariable Long id) throws DataNotFoundException {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(DeleteResponse.builder()
                .message("Category deleted successfully")
                .build());
    }
}
