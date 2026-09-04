package com.example.premiumorder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/premium-orders")
public class PremiumOrderController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> processPremiumOrder(@RequestBody Map<String, Object> request) {
        System.out.println("[PREMIUM ORDER SERVICE] Processing VIP Order request: " + request);

        Map<String, Object> response = new HashMap<>();
        response.put("requestId", request.getOrDefault("requestId", "REQ-VIP-" + System.currentTimeMillis()));
        response.put("status", "SUCCESS_VIP_PRIORITY_PROCESSED");
        response.put("serviceTier", "PREMIUM_VIP");
        response.put("slaTarget", "< 100ms");
        response.put("processedBy", "PremiumOrderMicroservice (Port 8081)");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("details", "VIP order processed with priority queue handling and premium discount applied.");

        return ResponseEntity.ok(response);
    }
}
