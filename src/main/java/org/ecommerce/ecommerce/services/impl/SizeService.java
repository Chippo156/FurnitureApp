package org.ecommerce.ecommerce.services.impl;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.SizeDTO;
import org.ecommerce.ecommerce.models.ProductSize;
import org.ecommerce.ecommerce.repository.ProductRepository;
import org.ecommerce.ecommerce.repository.SizeRepository;
import org.ecommerce.ecommerce.services.iSizeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeService implements iSizeService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productService;
    @Override
    public ProductSize createSize(SizeDTO sizeDTO) {
        ProductSize size = new ProductSize();
        size.setSize(sizeDTO.getSize());
        size.setPriceSize(sizeDTO.getPriceSize());
        size.setProduct(productService.findById(sizeDTO.getProductId()).orElseThrow(()->new RuntimeException("Product not found")));
        return sizeRepository.save(size);
    }
}
