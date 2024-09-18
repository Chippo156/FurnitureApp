package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.dtos.CartItemDTO;
import org.ecommerce.ecommerce.dtos.OrderDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.*;
import org.ecommerce.ecommerce.repository.OrderRepository;
import org.ecommerce.ecommerce.repository.ProductRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.responses.OrderResponse;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.iOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService implements iOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {

            User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found"));
            modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(
                    mapper -> mapper.skip(Order::setId)
            );
            Order order = new Order();
            modelMapper.map(orderDTO, order);
            order.setUser(existingUser);
            order.setActive(true);
            order.setOrderDate(new Date());
            order.setStatus(OrderStatus.PENDING);
            Date shippingDate = orderDTO.getShippingDate() == null ? new Date() : orderDTO.getShippingDate();
            if (shippingDate.before(new Date())) {
                throw new DataNotFoundException("Data must be at least today");
            }
            order.setShippingDate(shippingDate);

           List<CartItemDTO> cartItem = orderDTO.getCartItems();
           List<OrderDetail> orderDetails = new ArrayList<>();
           for (CartItemDTO Item:cartItem)
           {
               Product product = productRepository.findById(Item.getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setNumberOfProducts(Item.getQuantity());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setOrder(order);
                orderDetails.add(orderDetail);
           }
            order.setOrderDetails(orderDetails);
            orderRepository.save(order);
            return order;

    }

    @Override
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException {

            Order existingOrder = orderRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + orderId));
            User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> {
            mapper.skip(Order::setId);
        });
            modelMapper.map(orderDTO, existingOrder);
            existingOrder.setUser(existingUser);
            return orderRepository.save(existingOrder);

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId).orElse(null);
        if (existingOrder != null) {
            existingOrder.setActive(false);
            existingOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(existingOrder);
        }
    }
    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findOrderByUserId(userId).stream().map(OrderResponse::fromOrder).toList();
    }

    @Override
    public Page<OrderResponse> getAllOrders(String keyword, Pageable pageable) {
        Page<OrderResponse> orderResponses = orderRepository.getAllOrders(keyword, pageable).map(OrderResponse::fromOrder);
        return orderResponses;
    }

    @Override
    public List<ProductResponse> getProductsByOrderId(Long orderId) {
       return orderRepository.getProductsByOrderId(orderId).stream().map(ProductResponse::fromProduct).toList();
    }
    @Override
    public List<OrderResponse> getCancelledOrders(Long userId) {
        return orderRepository.getCancelledOrders(userId).stream().map(OrderResponse::fromOrder).toList();
    }
}
