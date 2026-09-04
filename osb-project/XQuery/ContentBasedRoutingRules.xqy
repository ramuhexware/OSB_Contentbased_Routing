(:: WSDL Ref="RequestRouter.wsdl" ::)
xquery version "1.0" encoding "utf-8";

(:: RootElement="RoutingRequest" Namespace="http://xmlns.oracle.com/osb/routing/payload" ::)

declare namespace ns1 = "http://xmlns.oracle.com/osb/routing/payload";

declare variable $req as element(ns1:RoutingRequest) external;
declare variable $targetService as xs:string external;

declare function local:transformPayload($req as element(ns1:RoutingRequest), $targetService as xs:string) 
    as element(ns1:RoutingResponse) {
    <ns1:RoutingResponse>
        <ns1:requestId>{ fn:data($req/ns1:Header/ns1:requestId) }</ns1:requestId>
        <ns1:status>ROUTED_SUCCESSFULLY</ns1:status>
        <ns1:routedToService>{ $targetService }</ns1:routedToService>
        <ns1:processedBy>OSB_ContentBasedRouter_Pipeline</ns1:processedBy>
        <ns1:customerTier>{ fn:data($req/ns1:Header/ns1:customerTier) }</ns1:customerTier>
        <ns1:responseDetails>{ fn:concat("Payload for customer ", $req/ns1:Payload/ns1:customerId, " routed based on content criteria.") }</ns1:responseDetails>
        <ns1:timestamp>{ fn:current-dateTime() }</ns1:timestamp>
    </ns1:RoutingResponse>
};

local:transformPayload($req, $targetService)
