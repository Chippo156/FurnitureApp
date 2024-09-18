package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.models.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {

}
