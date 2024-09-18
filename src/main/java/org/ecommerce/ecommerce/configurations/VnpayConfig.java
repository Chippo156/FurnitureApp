package org.ecommerce.ecommerce.configurations;

import lombok.Getter;
import org.ecommerce.ecommerce.untils.VnpayUntils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
public class VnpayConfig {


    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode;
    @Getter
    @Value("${payment.vnPay.hashSecret}")
    private String secretKey;

    @Value("${payment.vnPay.version}")
    private String vnp_Version;

    @Value("${payment.vnPay.command}")
    private String vnp_Command;
    @Value("${payment.vnPay.order_type}")
    private String orderType;

    public Map<String, String> getVnPayConfig()
    {
        Map<String, String> vnPayParamsMap = new HashMap<>();
        vnPayParamsMap.put("vnp_Version", vnp_Version);
        vnPayParamsMap.put("vnp_Command", vnp_Command);
        vnPayParamsMap.put("vnp_TmnCode", vnp_TmnCode);
        vnPayParamsMap.put("vnp_Locale", "vn");
        vnPayParamsMap.put("vnp_CurrCode", "VND");
        vnPayParamsMap.put("vnp_TxnRef", VnpayUntils.getRandomNumber(8));
        vnPayParamsMap.put("vnp_OrderType", orderType);
        vnPayParamsMap.put("vnp_ReturnUrl", vnp_ReturnUrl);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(calendar.getTime());
        vnPayParamsMap.put("vnp_CreateDate", vnp_CreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnPayParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnPayParamsMap;
    }
}
