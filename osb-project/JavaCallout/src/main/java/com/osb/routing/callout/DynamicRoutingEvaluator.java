package com.osb.routing.callout;

import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic Routing Evaluator for OSB Java Callouts.
 * Inspects request header details (serviceType and customerTier) and resolves
 * the appropriate target Business Service URI / endpoint.
 */
public class DynamicRoutingEvaluator {

    private static final Map<String, String> ROUTING_TABLE = new HashMap<>();

    static {
        // Map combinations of serviceType + customerTier to target endpoint URLs
        ROUTING_TABLE.put("ORDER:PREMIUM", "http://localhost:8081/api/premium-orders");
        ROUTING_TABLE.put("ORDER:STANDARD", "http://localhost:8082/api/standard-orders");
        ROUTING_TABLE.put("PAYMENT:PREMIUM", "http://localhost:8083/api/payments");
        ROUTING_TABLE.put("PAYMENT:STANDARD", "http://localhost:8083/api/payments");
    }

    /**
     * Determines target URI based on service type and customer tier.
     *
     * @param serviceType E.g. "ORDER", "PAYMENT"
     * @param customerTier E.g. "PREMIUM", "STANDARD"
     * @return Target Business Service Endpoint URI
     */
    public static String evaluateTargetEndpoint(String serviceType, String customerTier) {
        if (serviceType == null || serviceType.trim().isEmpty()) {
            serviceType = "ORDER";
        }
        if (customerTier == null || customerTier.trim().isEmpty()) {
            customerTier = "STANDARD";
        }

        String key = serviceType.toUpperCase().trim() + ":" + customerTier.toUpperCase().trim();
        String targetUrl = ROUTING_TABLE.get(key);

        if (targetUrl == null) {
            // Default fallback routing logic
            if ("ORDER".equalsIgnoreCase(serviceType)) {
                return "http://localhost:8082/api/standard-orders";
            } else {
                return "http://localhost:8083/api/payments";
            }
        }
        return targetUrl;
    }

    /**
     * Helper to resolve logical OSB Business Service Name.
     */
    public static String evaluateBusinessServiceName(String serviceType, String customerTier) {
        if ("ORDER".equalsIgnoreCase(serviceType)) {
            if ("PREMIUM".equalsIgnoreCase(customerTier)) {
                return "BusinessServices/PremiumOrderMicroservice";
            } else {
                return "BusinessServices/StandardOrderMicroservice";
            }
        } else if ("PAYMENT".equalsIgnoreCase(serviceType)) {
            return "BusinessServices/PaymentMicroservice";
        }
        return "BusinessServices/StandardOrderMicroservice";
    }
}
