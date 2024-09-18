package org.ecommerce.ecommerce.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.configurations.VnpayConfig;
import org.ecommerce.ecommerce.dtos.PaymentDTO;
import org.ecommerce.ecommerce.untils.VnpayUntils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VnpayConfig vnpayConfig;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request)
    {
        double amount  = Double.parseDouble(request.getParameter("amount"))*100L;
        Long roundAmount = Math.round(amount);
        String  bankCode = request.getParameter("bankCode");
        Map<String, String> vnPayParamsMap = vnpayConfig.getVnPayConfig();
        vnPayParamsMap.put("vnp_Amount", String.valueOf(roundAmount));
        if(bankCode != null && !bankCode.isEmpty())
        {
            vnPayParamsMap.put("vnp_BankCode", bankCode);
        }
        vnPayParamsMap.put("vnp_OrderInfo", request.getParameter("orderId"));
        vnPayParamsMap.put("vnp_IpAddr", VnpayUntils.getIpAddress(request));
        //build query url
        String queryUrl = VnpayUntils.getPaymentURL(vnPayParamsMap,true);
        String hashData = VnpayUntils.getPaymentURL(vnPayParamsMap,false);
        String vnpSecureHash = VnpayUntils.hmacSHA512(vnpayConfig.getSecretKey(),hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("Success")
                .paymentUrl(paymentUrl)
                .build();
    }


}
