package org.ecommerce.ecommerce.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.models.Category;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.iCategoryRedisService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryRedisService implements iCategoryRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    private String getKeyFrom(String keyword, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id")).getDirection() == Sort.Direction.ASC ? "ASC" : "DESC";
        String key = String.format("all_categories:%d:%d:%s:%s", pageNumber, pageSize, sortDirection, keyword);
        return key;
    }

    @Override
    public List<Category> getAllCategories(String keyword, PageRequest pageRequest) throws Exception {
        String key = this.getKeyFrom(keyword, pageRequest);
        String json = redisTemplate.opsForValue().get(key);
        List<Category> categories =
                json != null ?  redisObjectMapper.readValue(json, new TypeReference<List<Category>>() {})
                 : null;
        return categories;
    }

    @Override
    public void saveAllCategories(List<Category> categories,String keyword, PageRequest pageRequest) throws Exception {
        String key = this.getKeyFrom(keyword, pageRequest);
        String json = redisObjectMapper.writeValueAsString(categories);
        redisTemplate.opsForValue().set(key, json);

    }
}
