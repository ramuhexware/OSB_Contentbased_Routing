package com.example.standardorder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/standard-orders")
public class StandardOrderController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> processStandardOrder(@RequestBody Map<String, Object> request) {
        System.out.println("[STANDARD ORDER SERVICE] Processing Standard Order request: " + request);

        Map<String, Object> response = new HashMap<>();
        response.put("requestId", request.getOrDefault("requestId", "REQ-STD-" + System.currentTimeMillis()));
        response.put("status", "SUCCESS_STANDARD_PROCESSED");
        response.put("serviceTier", "STANDARD");
        response.put("slaTarget", "< 2000ms");
        response.put("processedBy", "StandardOrderMicroservice (Port 8082)");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("details", "Standard order processed via default queue handling.");

        return ResponseEntity.ok(response);
    }
}
