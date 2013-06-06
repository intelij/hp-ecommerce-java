# Handpoint E-commerce Java Client
=================

## Introduction
This is a java client library to integrate with Handpoint E-Commerce API

## Installation
For Maven, add the following dependency to your POM:
```xml
<dependency>
    <groupId>com.handpoint</groupId>
    <artifactId>ecommerce-client-java</artifactId>
    <version>1.0.0</version>
</dependency>
```
If you are not using maven you can download it and its dependencies manually and include it in your project

1. [ecommerce-client-java-1.0.0-javadoc.jar](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.0/ecommerce-client-java-1.0.0-javadoc.jar)
2. [ecommerce-client-java-1.0.0-sources.jar](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.0/ecommerce-client-java-1.0.0-sources.jar)
3. [ecommerce-client-java-1.0.0](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.0/ecommerce-client-java-1.0.0.jar)
4. [hibernate-validator](http://repo1.maven.org/maven2/org/hibernate/hibernate-validator/4.3.1.Final/hibernate-validator-4.3.1.Final.jar)
5. [jersey-client](http://repo1.maven.org/maven2/com/sun/jersey/jersey-client/1.8/jersey-client-1.8.bundle)

## Usage
Here below are examples on how to initalize client and use for different kind of operations.
Please take a look at ECommerceClientIT for more information [EcommerceClientIT](https://github.com/handpoint/hp-ecommerce-java/blob/master/src/test/java/com/handpoint/ecommerce/core/ECommerceClientIT.java)

### Initialize ECommerce Client
Instance ECommerceClient with values provided by Handpoint, for test environment, use the following values:

```java
public static final String CARD_ACCEPTOR = "7f6451e8314defbb50d0";
public static final and SHARED_SECRET = "8F10C8AD35B7AEC11675B50DBF6ACEAA0B4EC280B92500E51A02F7BBBE7B07C6"

// Instance client to test Environment without Http logging filter.
ECommerceClient client = new ECommerceClient(CARD_ACCEPTOR, SHARED_SECRET, Environment.TEST);

// Instance client to test Environment including logging filter.
ECommerceClient client = new ECommerceClient(CARD_ACCEPTOR, SHARED_SECRET, Environment.TEST, Boolean.TRUE);
```
### Operations
Note that all functions use the ECommerce client. See how it is initialized above.
In the examples below, variables are used to pass in to the client. Example values:

```java
String amount = "70";
String cardNumber = "4222222222222";
String expiryDate = "1215";
String token = "TOKEN_1";
String cardVerificationCode = "123"
String currency = Currency.ISK.alpha;
```

1. Authorization

```java
// Authorization using card number and expiry date
Authorization authorization = client.authorize(currency, amount, cardNumber, expiryDate);
// Authorization using card number, expiry date and card verification code
Authorization authorization = client.authorizeWithCVC(currency, amount, cardNumber, expiryDate, cardVerificationCode);
// Authorization using token
Authorization authorization = client.authorizeWithToken(currency, amount, token);
// Authorize and store card number information (token)
authorization = client.authorizeAndStoreToken(currency, amount, cardNumber, expiryDate, token)
```

2. Authorization + Payment
```java
// Capture previously approved authorization
Authorization authorization = client.authorize(currency, amount, cardNumber, expiryDate);
Payment payment = client.captureAuthorization(currency, amount, authorization.getAuthorizationGuid());
```

3. Payment
```java
// Payment using card number and expiry date
Payment payment = client.payment(currency, amount, cardNumber, expiryDate);
// Payment using card number, expiry date and card verification code
Payment payment = client.paymentWithCVC(currency, amount, cardNumber, expiryDate, cardVerificationCode);
// Payment using token
Payment payment = client.paymentWithToken(currency, amount, token);
// Payment and storing card number information (token)
Payment payment = client.paymentAndStoreToken(currency, amount, carNumber, expiryDate, token);
```

4. Authorization + Cancellation
```java
try {
    Authorization authorization = client.authorize(currency, amount, cardNumber, expiryDate);
catch(HpEcommerceException e) {
    Cancellation cancellation = client.cancelAuthorization(currency, amount, e.terminalDateTime());
 }
```


5. Refund
```java
Refund refund = client.refund(currency, amount, cardNumber, expiryDate);
```

6. Linked refund
```java
Payment payment = client.payment(currency, amount, cardNumber, expiryDate);
Refund refund = client.refundPayment(currency, amount, payment.getPaymentGuid());
```

7. Reversal
```java
// Reverse authorization
Authorization authorization = client.authorize(currency, amount, cardNumber, expiryDate);
Reversal reversal = client.reverseAuthorization(authorization.getAuthorizationGuid());
// Reverse Payment
Payment payment = client.payment(currency, amount, cardNumber, expiryDate);
Reversal reversal = client.reversePayment(payment.getPaymentGuid());
// Reverse refund
Refund refund = client.refund(currency, amount, cardNumber, expiryDate);
Reversal reversal = client.reverseRefund(refund.getRefundGuid());
```

8. Get Token
```java
Token token = client.getToken("token_identifier");
```

9. Create token
```java
Token token = client.createToken("token_identifier", cardNumber, expiryDate);
```

10. Update token
```java
Token token = client.updateToken("token_identifier", cardNumber, expiryDate);
```

11. Delete Token
```java
Token token = client.deleteToken("token_identifier");
```






