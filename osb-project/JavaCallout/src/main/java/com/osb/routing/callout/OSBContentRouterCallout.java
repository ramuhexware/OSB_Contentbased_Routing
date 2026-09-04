package com.osb.routing.callout;

/**
 * Entry-point Java Callout class invoked from OSB Pipeline actions.
 * OSB Java Callouts require static methods that take primitives or String arguments
 * and return String/object results to OSB pipeline variables.
 */
public class OSBContentRouterCallout {

    /**
     * Called directly by OSB Pipeline Java Callout Action.
     * 
     * @param serviceType Inbound message header serviceType (e.g. ORDER, PAYMENT)
     * @param customerTier Inbound message header customerTier (e.g. PREMIUM, STANDARD)
     * @return Target Business Service endpoint URL string.
     */
    public static String getRouteDestination(String serviceType, String customerTier) {
        System.out.println("[OSB Java Callout] Evaluating routing for serviceType=" 
                + serviceType + ", customerTier=" + customerTier);
        
        String targetEndpoint = DynamicRoutingEvaluator.evaluateTargetEndpoint(serviceType, customerTier);
        
        System.out.println("[OSB Java Callout] Target Endpoint resolved: " + targetEndpoint);
        return targetEndpoint;
    }

    /**
     * Called by OSB Pipeline Java Callout Action to resolve Business Service Name.
     */
    public static String getBusinessServiceName(String serviceType, String customerTier) {
        return DynamicRoutingEvaluator.evaluateBusinessServiceName(serviceType, customerTier);
    }
}
