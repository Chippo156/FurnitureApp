package org.ecommerce.ecommerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.OrderDTO;
import org.ecommerce.ecommerce.dtos.PaymentDTO;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Order;
import org.ecommerce.ecommerce.services.impl.OrderService;
import org.ecommerce.ecommerce.services.impl.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/payment")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping("/vn-pay")
    public ResponseEntity<?> pay(HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vnpay-callback")
    public ResponseEntity<?> payCallBackHandle(HttpServletRequest request, HttpServletResponse response) throws DataNotFoundException, IOException {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            Long orderId = Long.parseLong(request.getParameter("vnp_OrderInfo"));
            Order order = orderService.getOrderById(orderId);
            order.setStatus("processing");
            order.setActive(true);
            orderService.updateOrder(orderId,modelMapper.map(order, OrderDTO.class));
            response.sendRedirect("http://localhost:4200/?paymentStatus=00");
            return ResponseEntity.ok(response.getStatus());
        }else{
            response.sendRedirect("http://localhost:4200/?paymentStatus=01");
            return ResponseEntity.ok("Thanh toán thất bại");
        }
    }
}
