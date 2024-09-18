package org.ecommerce.ecommerce.services.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.iProductRedisService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements iProductRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id")).getDirection() == Sort.Direction.ASC ? "ASC" : "DESC";
        String key = String.format("all_products:%d:%d:%s:%s:%d", pageNumber, pageSize, sortDirection, keyword,categoryId);
        return key;
    }
    private String getKeyFromProductId(Long productId) {
        return String.format("product:%d", productId);
    }


    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisTemplate.opsForValue().get(key);
        List<ProductResponse> productResponses =
                json != null ? redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {})
                        : null;
        return productResponses;
    }

    @Override
    public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public ProductResponse getProductById(Long productId) throws JsonProcessingException {
        String key = this.getKeyFromProductId(productId);
        String json = redisTemplate.opsForValue().get(key);
        ProductResponse productResponse = json != null ? redisObjectMapper.readValue(json, ProductResponse.class) : null;
        return productResponse;
    }
    @Override
    public void saveProduct(ProductResponse productResponse) throws JsonProcessingException {
        String key = this.getKeyFromProductId(productResponse.getId());
        String json = redisObjectMapper.writeValueAsString(productResponse);
        redisTemplate.opsForValue().set(key, json);
    }


}
