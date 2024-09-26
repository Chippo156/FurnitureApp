package org.ecommerce.ecommerce.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ecommerce.ecommerce.dtos.ProductDTO;
import org.ecommerce.ecommerce.dtos.ProductImageDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.exceptions.InvalidParamException;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.models.ProductImage;
import org.ecommerce.ecommerce.responses.CommentResponse;
import org.ecommerce.ecommerce.responses.ProductListResponse;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ProductRedisService productRedisService;
    @Autowired
    private CommentService commentService;

    @PostMapping (value = "/uploadImages/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> uploadImage(@PathVariable Long id,@ModelAttribute("files") List<MultipartFile> files) throws InvalidParamException, DataNotFoundException {
        Product product = productService.getProductById(id);
        List<String> urls = cloudinaryService.upload(files);
        for (String url : urls) {
            ProductImage productImage = productService.createProductImage(product.getId(),
                    ProductImageDTO.builder()
                            .image_url(url)
                            .build()
            );
            if (product.getThumbnail().isEmpty()) {
                product.setThumbnail(productImage.getImage_url());
                productService.updateProduct(product.getId(), ProductDTO.builder().thumbnail(productImage.getImage_url()).build());
            }
        }
        return ResponseEntity.ok().body(product);
    }
    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam int page, @RequestParam int limit) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        List<ProductResponse> productResponses = productRedisService.getAllProducts(keyword, categoryId, pageRequest);
        int totalPages = 5;
        if (productResponses == null) {
            Page<ProductResponse> products = productService.getAllProducts(keyword, categoryId, pageRequest);
            totalPages = products.getTotalPages();
            productResponses = products.getContent();
            productRedisService.saveAllProducts(productResponses, keyword, categoryId, pageRequest);
        }

        return ResponseEntity.ok(ProductListResponse.builder()
                .productResponses(productResponses)
                .totalPage(totalPages)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) throws JsonProcessingException {

        ProductResponse productResponse = productRedisService.getProductById(id);
        if (productResponse == null) {

            productRedisService.saveProduct(ProductResponse.fromProduct(productService.getProductById(id)));
            return ResponseEntity.ok(ProductResponse.fromProduct(productService.getProductById(id)));
        }
        return ResponseEntity.ok(productResponse);

    }
    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream().map(FieldError::getField).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            productService.createProduct(productDTO);
            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream().map(FieldError::getField).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Product product = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Product update failed");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Product deletion failed");
        }
    }
    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            List<Long> productIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
            List<ProductResponse> products = productService.getProductByIds(productIds);
            return ResponseEntity.ok(ProductListResponse.builder()
                    .productResponses(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-classify_color")
    public ResponseEntity<?> getProductsByCategoryIdAndColors(@RequestParam("classify_color_id") Long classify_color_id) {
        try {
            List<ProductResponse> productResponses = productService.getProductsByClassifyId(classify_color_id);
            return ResponseEntity.ok(ProductListResponse.builder()
                    .productResponses(productResponses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-category-name")
    public ResponseEntity<?> getProductsByCategoryName(@RequestParam("categoryName") String categoryName) {
        try {
            List<ProductResponse> productResponses = productService.getProductsByCategoryName(categoryName);
            return ResponseEntity.ok(ProductListResponse.builder()
                    .productResponses(productResponses)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/rating-products")
    public ResponseEntity<?> getRatingProducts() {
        try {
            return ResponseEntity.ok(productService.getRatingProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/sizes/{id}")
    public ResponseEntity<?> getProductSizesByProductId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductSizesByProductId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getCommentsByProductId(@PathVariable Long id)
    {
        try {
            List<CommentResponse> commentResponses = commentService.getCommentsByProductId(id);
            return ResponseEntity.ok(commentResponses);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}

