package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Order;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId and o.status != 'CANCELLED' and o.active = true  order by o.orderDate desc")
    List<Order> findOrderByUserId(@Param("userId") Long userId);
    @Query("SELECT o FROM Order o WHERE (:keyword IS NULL OR :keyword = '' OR " +
            "o.fullName LIKE %:keyword% OR o.address " +
            "LIKE %:keyword% OR o.note LIKE %:keyword%  OR o.email LIKE %:keyword%) and o.active = true ORDER BY o.orderDate DESC")
    Page<Order> getAllOrders(@Param("keyword") String keyword,
                             Pageable pageable);
    @Query("select od.product from OrderDetail od where od.order.id = :orderId")
    List<Product> getProductsByOrderId(@Param("orderId") Long orderId);

    @Query("select o from Order o where o.user.id= :userId  and o.status = 'CANCELLED' order by o.orderDate desc" )
    List<Order> getCancelledOrders(@Param("userId") Long userId);
}
