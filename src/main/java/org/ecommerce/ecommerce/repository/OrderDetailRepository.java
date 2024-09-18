package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.OrderDetail;
import org.ecommerce.ecommerce.responses.CountProductByOrdersResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

    @Query("SELECT new org.ecommerce.ecommerce.responses.CountProductByOrdersResponse(c.product.id, SUM(c.numberOfProducts)) from OrderDetail c group by c.product.id order by c.product.id desc")
    List<CountProductByOrdersResponse> countNumberOfProduct();
}
