package com.example.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/client")
public class ClientTestController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${osb.proxy.url}")
    private String osbProxyUrl;

    /**
     * Test endpoint for sending client requests to OSB Router.
     * 
     * @param serviceType E.g. "ORDER", "PAYMENT"
     * @param customerTier E.g. "PREMIUM", "STANDARD"
     */
    @GetMapping("/send")
    public ResponseEntity<Map<String, Object>> sendClientRequest(
            @RequestParam(defaultValue = "ORDER") String serviceType,
            @RequestParam(defaultValue = "PREMIUM") String customerTier,
            @RequestParam(defaultValue = "CUST-9901") String customerId,
            @RequestParam(defaultValue = "250.00") double amount) {

        System.out.println("[CLIENT SERVICE] Dispatching request to OSB Proxy Service: " + osbProxyUrl);

        Map<String, Object> header = new HashMap<>();
        header.put("requestId", "REQ-" + UUID.randomUUID().toString().substring(0, 8));
        header.put("timestamp", java.time.LocalDateTime.now().toString());
        header.put("serviceType", serviceType);
        header.put("customerTier", customerTier);

        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", customerId);
        payload.put("amount", amount);
        payload.put("items", "Item-A, Item-B");
        payload.put("notes", "Client test order dispatch via OSB Content-Based Router");

        Map<String, Object> requestEnvelope = new HashMap<>();
        requestEnvelope.put("header", header);
        requestEnvelope.put("payload", payload);

        @SuppressWarnings("unchecked")
        Map<String, Object> osbResponse = restTemplate.postForObject(osbProxyUrl, requestEnvelope, Map.class);

        return ResponseEntity.ok(osbResponse);
    }
}
