package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.ProductDTO;
import org.ecommerce.ecommerce.dtos.ProductImageDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.exceptions.InvalidParamException;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.models.ProductImage;
import org.ecommerce.ecommerce.models.ProductSize;
import org.ecommerce.ecommerce.responses.ProductRatingResponse;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface iProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product updateProduct(Long productId, ProductDTO productDTO) throws Exception;
    Product getProductById(Long productId);
    void deleteProduct(Long productId) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws DataNotFoundException;

    boolean existsByProductName(String productName);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
    List<ProductResponse> getProductByIds(List<Long> productIds);

    List<ProductResponse> getProductsByClassifyId(Long categoryId);

    List<ProductResponse> getProductsByCategoryName(String categoryName);
    List<ProductRatingResponse> getRatingProducts();
    List<ProductSize> getProductSizesByProductId(Long productId);



}
