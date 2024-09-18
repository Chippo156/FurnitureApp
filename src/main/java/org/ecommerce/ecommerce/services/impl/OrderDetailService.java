package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.dtos.OrderDetailDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Order;
import org.ecommerce.ecommerce.models.OrderDetail;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.repository.OrderDetailRepository;
import org.ecommerce.ecommerce.repository.OrderRepository;
import org.ecommerce.ecommerce.repository.ProductRepository;
import org.ecommerce.ecommerce.responses.CountProductByOrdersResponse;
import org.ecommerce.ecommerce.responses.OrderDetaiResponse;
import org.ecommerce.ecommerce.services.iOrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderDetailService implements iOrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(() -> new DataNotFoundException("Order not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalPrice());
        orderDetail.setSubtotal(orderDetailDTO.getSubtotal());
        orderDetailRepository.save(orderDetail);
        return orderDetail;
    }

    @Override
    public OrderDetail getOrderDetailById(Long orderDetailId) throws DataNotFoundException {
        return orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new DataNotFoundException("Order detail not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrder = orderDetailRepository.findById(orderDetailId).orElseThrow(()-> new DataNotFoundException("Order detail not found"));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(() -> new DataNotFoundException("Order not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));
        existingOrder.setProduct(product);
        existingOrder.setOrder(order);
        existingOrder.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrder.setPrice(orderDetailDTO.getPrice());
        existingOrder.setTotalMoney(orderDetailDTO.getTotalPrice());
        existingOrder.setSubtotal(orderDetailDTO.getSubtotal());
        orderDetailRepository.save(existingOrder);
        return existingOrder;
    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) throws DataNotFoundException {
       OrderDetail existingOrder = orderDetailRepository.findById(orderDetailId).orElseThrow(() -> new DataNotFoundException("Order detail not found"));
         orderDetailRepository.delete(existingOrder);
    }

    @Override
    public List<OrderDetaiResponse> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream().map(OrderDetaiResponse::fromOrderDetail).toList();
    }

    @Override
    public List<CountProductByOrdersResponse> countNumberOfProduct() {
       return orderDetailRepository.countNumberOfProduct();
    }
}
