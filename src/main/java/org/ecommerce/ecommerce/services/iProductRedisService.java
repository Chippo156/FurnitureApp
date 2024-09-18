package org.ecommerce.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface iProductRedisService {

    void clear();

    List<ProductResponse> getAllProducts(
            String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException;

    ProductResponse getProductById(Long productId) throws JsonProcessingException;

    void saveProduct(ProductResponse productResponse) throws JsonProcessingException;


}
