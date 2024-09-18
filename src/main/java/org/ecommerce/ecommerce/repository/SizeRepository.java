package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.ProductSize;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<ProductSize, Long>{
}
