package com.example.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> request) {
        System.out.println("[PAYMENT SERVICE] Processing Payment request: " + request);

        Map<String, Object> response = new HashMap<>();
        response.put("requestId", request.getOrDefault("requestId", "REQ-PAY-" + System.currentTimeMillis()));
        response.put("status", "SUCCESS_PAYMENT_PROCESSED");
        response.put("processedBy", "PaymentProviderMicroservice (Port 8083)");
        response.put("transactionId", "TXN-" + System.currentTimeMillis());
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("details", "Payment transaction validated and completed.");

        return ResponseEntity.ok(response);
    }
}
