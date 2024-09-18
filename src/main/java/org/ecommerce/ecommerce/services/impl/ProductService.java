package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.dtos.ProductDTO;
import org.ecommerce.ecommerce.dtos.ProductImageDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.exceptions.InvalidParamException;
import org.ecommerce.ecommerce.models.*;
import org.ecommerce.ecommerce.repository.*;
import org.ecommerce.ecommerce.responses.ProductRatingResponse;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.iProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ProductService implements iProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Autowired
    private SizeRepository productSizeRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(()
                -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId())
        );
        ProductSale productSale = productSaleRepository.findById(productDTO.getSaleId()).orElseThrow(()
                -> new DataNotFoundException("Cannot find sale with id: " + productDTO.getSaleId()));
        Product product = Product.builder()
                .name(productDTO.getProductName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .thumbnail(productDTO.getThumbnail())
                .category(category)
                .sale(productSale)
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductDTO productDTO) throws DataNotFoundException {
        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Cannot find product with id: " + productId));
            product.setName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            if (productDTO.getDescription() != null) {
                product.setDescription(productDTO.getDescription());
            }
            if (productDTO.getThumbnail() != null) {
                product.setThumbnail(productDTO.getThumbnail());
            }
            if (productDTO.getCategoryId() != null) {
                Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));
                product.setCategory(category);
            }
            if (productDTO.getSaleId() != null) {
                ProductSale productSale = productSaleRepository.findById(productDTO.getSaleId()).orElseThrow(()
                        -> new DataNotFoundException("Cannot find sale with id: " + productDTO.getSaleId()));
                product.setSale(productSale);
            }
            if(productDTO.getSize() != null)
            {
                product.getProductSizes().forEach((size) ->  {
                    if(Objects.equals(size.getId(), productDTO.getSize().getId()))
                    {
                        size.setSize(productDTO.getSize().getSize());
                    }
                });
            }
            product.setProductImages(product.getProductImages());
            product.setComments(product.getComments());
            return productRepository.saveAndFlush(product);
        } catch (Exception e) {
            throw new DataNotFoundException("Lỗi ");
        }
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void deleteProduct(Long productId) throws DataNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        return productRepository.searchProducts(keyword,categoryId, pageRequest).map(ProductResponse::fromProduct);
    }


    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByName(productName);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + productId));
        ProductImage productImage = ProductImage.builder()
                .product(product)
                .image_url(productImageDTO.getImage_url())
                .build();

        int size = productImageRepository.findAllByProductId(productId).size();
        if (size >= 5) {
            throw new InvalidParamException("Cannot add more than 5 images");
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public List<ProductResponse> getProductByIds(List<Long> productIds) {
        try {
            return productRepository.getProductByIds(productIds).stream().map(ProductResponse::fromProduct).toList();
        } catch (Exception e) {
            throw new RuntimeException("Product not found");
        }
    }

    @Override
    public List<ProductResponse> getProductsByClassifyId(Long classify_color_id) {
        return productRepository.findAllByClassifyColorId(classify_color_id).stream().map(ProductResponse::fromProduct).toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategoryName(String categoryName) {
        return productRepository.findAllByCategoryName(categoryName).stream().map(ProductResponse::fromProduct).toList();
    }

    @Override
    public List<ProductRatingResponse> getRatingProducts() {
        return productRepository.getRatingProducts();
    }

    @Override
    public List<ProductSize> getProductSizesByProductId(Long productId) {
        return productRepository.getProductSizesByProductId(productId);
    }


}
