package com.example.osb.router.callout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Simulates OSB Java Callout & Pipeline Branching evaluation logic.
 */
@Component
public class OSBJavaCalloutEvaluator {

    @Value("${routing.target-endpoints.premium-order}")
    private String premiumOrderEndpoint;

    @Value("${routing.target-endpoints.standard-order}")
    private String standardOrderEndpoint;

    @Value("${routing.target-endpoints.payment}")
    private String paymentEndpoint;

    /**
     * Inspects inbound request payload content (Header -> serviceType, customerTier)
     * and evaluates target destination endpoint.
     */
    public String evaluateTargetUrl(Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        Map<String, Object> header = (Map<String, Object>) payload.get("header");

        String serviceType = "ORDER";
        String customerTier = "STANDARD";

        if (header != null) {
            if (header.containsKey("serviceType")) {
                serviceType = String.valueOf(header.get("serviceType")).toUpperCase().trim();
            }
            if (header.containsKey("customerTier")) {
                customerTier = String.valueOf(header.get("customerTier")).toUpperCase().trim();
            }
        }

        System.out.println("[OSB Pipeline Java Callout] Evaluating content: serviceType=" 
                + serviceType + ", customerTier=" + customerTier);

        // Content-Based Routing Rules:
        if ("ORDER".equals(serviceType)) {
            if ("PREMIUM".equals(customerTier)) {
                System.out.println("[OSB Branch Action] Selected Route: PremiumOrderMicroservice (VIP)");
                return premiumOrderEndpoint;
            } else {
                System.out.println("[OSB Branch Action] Selected Route: StandardOrderMicroservice");
                return standardOrderEndpoint;
            }
        } else if ("PAYMENT".equals(serviceType)) {
            System.out.println("[OSB Branch Action] Selected Route: PaymentProviderMicroservice");
            return paymentEndpoint;
        }

        // Fallback default route
        return standardOrderEndpoint;
    }
}
