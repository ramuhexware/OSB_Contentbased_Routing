# Oracle Service Bus (OSB) Content-Based Routing (CBR) with Java Microservices

An enterprise-grade implementation of the **Content-Based Router (CBR)** Enterprise Integration Pattern using **Oracle Service Bus (OSB)** metadata (XML, WSDL, XSD, XQuery, Java Callouts) alongside executable **Java Spring Boot Microservices** (Premium Order Service, Standard Order Service, Payment Service, Client Service, and OSB Router Simulator).

---

## 🏛️ Architecture Overview

```
                      +-----------------------------+
                      |   Client Microservice       |
                      |   Port 8084                 |
                      +--------------+--------------+
                                     |
                                     | POST Request (JSON / XML)
                                     v
                      +-----------------------------+
                      |  OSB Content-Based Router   |
                      |  (Proxy & Java Callout)     |
                      |  Port 8080                  |
                      +--------------+--------------+
                                     |
           +-------------------------+-------------------------+
           | Content Evaluation: serviceType & customerTier   |
           +-------------------------+-------------------------+
           |                         |                         |
  ORDER + PREMIUM             ORDER + STANDARD              PAYMENT
           |                         |                         |
           v                         v                         v
+--------------------+    +--------------------+    +--------------------+
| Premium Order      |    | Standard Order     |    | Payment Provider   |
| Service (Port 8081)|    | Service (Port 8082)|    | Service (Port 8083)|
| [VIP Priority SLA] |    | [Standard Queue]   |    | [Transaction Processing]
+--------------------+    +--------------------+    +--------------------+
```

---

## 📂 Project Structure

```
OSB_Contentbased_Routing/
├── osb-project/                                 # Oracle Service Bus Server Metadata
│   ├── WSDL/
│   │   └── RequestRouter.wsdl                  # SOAP/WSDL entry contract for OSB Proxy
│   ├── XSD/
│   │   └── RoutingPayload.xsd                  # XML Schema definition for request/response
│   ├── XQuery/
│   │   └── ContentBasedRoutingRules.xqy        # XQuery transformation rules
│   ├── JavaCallout/
│   │   └── src/main/java/com/osb/routing/callout/
│   │       ├── DynamicRoutingEvaluator.java    # Dynamic route evaluation logic
│   │       └── OSBContentRouterCallout.java    # Java Callout interface for OSB Pipeline
│   ├── BusinessServices/
│   │   ├── PremiumOrderMicroservice.biz        # Business Service XML for Port 8081
│   │   ├── StandardOrderMicroservice.biz       # Business Service XML for Port 8082
│   │   └── PaymentMicroservice.biz             # Business Service XML for Port 8083
│   └── Pipelines/
│       ├── ContentBasedRouting.proxy           # Proxy Service definition (XML)
│       └── ContentBasedRouting.pipeline        # Pipeline with Java Callout & Branch Nodes
│
├── java-microservices/                          # Spring Boot Microservices
│   ├── pom.xml                                 # Parent Maven POM
│   ├── client-service/ (Port 8084)             # Client application to send test requests
│   ├── premium-order-service/ (Port 8081)      # VIP Order Processing Microservice
│   ├── standard-order-service/ (Port 8082)     # Standard Order Processing Microservice
│   ├── payment-provider-service/ (Port 8083)   # Payment Processing Microservice
│   └── osb-router-service/ (Port 8080)         # Java OSB Router Engine (Simulates OSB locally)
│
└── README.md
```

---

## ⚙️ Configuration Specs: XML vs YAML

- **XML Configuration Files (`osb-project/`)**: Native Oracle Service Bus (OSB) server artifacts (`.proxy`, `.pipeline`, `.biz`, `.wsdl`, `.xsd`, `.xqy`). Import these directly into **Oracle JDeveloper** or **OEPE** for WebLogic OSB deployment.
- **YAML Configuration Files (`application.yml`)**: Used by the Java Spring Boot microservices to define server ports, endpoint mappings, and logging configurations.

---

## 🚀 How to Run and Test

### 1. Build the Java Microservices
From the `java-microservices` directory, build all modules using Maven:

```bash
cd java-microservices
mvn clean package -DskipTests
```

### 2. Start the Microservices
Start each service in a separate terminal:

1. **Premium Order Service (Port 8081)**:
   ```bash
   mvn -pl premium-order-service spring-boot:run
   ```
2. **Standard Order Service (Port 8082)**:
   ```bash
   mvn -pl standard-order-service spring-boot:run
   ```
3. **Payment Provider Service (Port 8083)**:
   ```bash
   mvn -pl payment-provider-service spring-boot:run
   ```
4. **OSB Router Service (Port 8080)**:
   ```bash
   mvn -pl osb-router-service spring-boot:run
   ```
5. **Client Service (Port 8084)**:
   ```bash
   mvn -pl client-service spring-boot:run
   ```

---

## 🧪 Testing Content-Based Routing Scenarios

### Scenario A: VIP Order Request (`serviceType=ORDER`, `customerTier=PREMIUM`)
**Trigger via Client Service:**
```bash
curl "http://localhost:8084/api/client/send?serviceType=ORDER&customerTier=PREMIUM&customerId=VIP-7001&amount=1500.00"
```
**Expected Outcome:**
The request is routed to **Premium Order Microservice (`http://localhost:8081/api/premium-orders`)**, returning `"status": "SUCCESS_VIP_PRIORITY_PROCESSED"`.

---

### Scenario B: Standard Order Request (`serviceType=ORDER`, `customerTier=STANDARD`)
**Trigger via Client Service:**
```bash
curl "http://localhost:8084/api/client/send?serviceType=ORDER&customerTier=STANDARD&customerId=CUST-1002&amount=49.99"
```
**Expected Outcome:**
The request is routed to **Standard Order Microservice (`http://localhost:8082/api/standard-orders`)**, returning `"status": "SUCCESS_STANDARD_PROCESSED"`.

---

### Scenario C: Payment Request (`serviceType=PAYMENT`)
**Trigger via Client Service:**
```bash
curl "http://localhost:8084/api/client/send?serviceType=PAYMENT&customerId=CUST-3050&amount=299.00"
```
**Expected Outcome:**
The request is routed to **Payment Provider Microservice (`http://localhost:8083/api/payments`)**, returning `"status": "SUCCESS_PAYMENT_PROCESSED"`.

---

### Direct HTTP Test to OSB Proxy Service (`http://localhost:8080/osb/proxy/ContentBasedRouting`)

```bash
curl -X POST http://localhost:8080/osb/proxy/ContentBasedRouting \
  -H "Content-Type: application/json" \
  -d '{
    "header": {
      "requestId": "REQ-VIP-9999",
      "serviceType": "ORDER",
      "customerTier": "PREMIUM"
    },
    "payload": {
      "customerId": "VIP-9999",
      "amount": 5000.00
    }
  }'
```
