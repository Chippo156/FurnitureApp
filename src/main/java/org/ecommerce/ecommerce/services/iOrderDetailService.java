package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.OrderDetailDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Order;
import org.ecommerce.ecommerce.models.OrderDetail;
import org.ecommerce.ecommerce.responses.CountProductByOrdersResponse;
import org.ecommerce.ecommerce.responses.OrderDetaiResponse;

import java.util.List;
import java.util.Map;

public interface iOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDetailById(Long orderDetailId) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(Long orderDetailId) throws DataNotFoundException;

    List<OrderDetaiResponse> findByOrderId(Long orderId);

   List<CountProductByOrdersResponse> countNumberOfProduct();






}
