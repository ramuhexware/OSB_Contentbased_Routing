package com.example.osb.router.controller;

import com.example.osb.router.callout.OSBJavaCalloutEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/osb/proxy")
public class OSBContentRouterController {

    @Autowired
    private OSBJavaCalloutEvaluator calloutEvaluator;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/ContentBasedRouting")
    public ResponseEntity<Map<String, Object>> handleContentBasedRouting(@RequestBody Map<String, Object> requestPayload) {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("[OSB Proxy Service] Received incoming request message");

        // Step 1: Java Callout Evaluation & Branching
        String targetUrl = calloutEvaluator.evaluateTargetUrl(requestPayload);

        // Step 2: Route Action to Target Business Service (HTTP Transport)
        System.out.println("[OSB Route Action] Dispatching HTTP POST request to Business Service URL: " + targetUrl);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(targetUrl, requestPayload, Map.class);
            
            if (response == null) {
                response = new HashMap<>();
                response.put("status", "EMPTY_RESPONSE_FROM_BACKEND");
            }
            
            response.put("osbRoutingStatus", "ROUTED_SUCCESSFULLY");
            response.put("targetEndpointUsed", targetUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("[OSB Error Pipeline] Routing failed: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "OSB_ROUTING_FAILURE");
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("targetEndpointAttempted", targetUrl);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
