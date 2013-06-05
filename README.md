# Handpoint E-commerce Java Client
=================

## Introduction
This is a java client library to integrate with Handpoint E-Commerce API

## Installation
    To be done

## Usage
Please take a look at ECommerceClientIT for more information (https://github.com/handpoint/hp-ecommerce-java/blob/master/src/test/java/com/handpoint/ecommerce/core/ECommerceClientIT.java)


### Initialize ECommerce Client
Instance ECommerceClient with values provided by Handpoint, for test environment, use the following values:

```java
public static final String CARD_ACCEPTOR = "7f6451e8314defbb50d0";
public static final and SHARED_SECRET = "8F10C8AD35B7AEC11675B50DBF6ACEAA0B4EC280B92500E51A02F7BBBE7B07C6"

ECommerceClient client = new ECommerceClient(CARD_ACCEPTOR, SHARED_SECRET, Environment.TEST);
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
```

1. Authorization

```java
// Authorization using card number and expiry date
Authorization authorization = client.authorize(Currency.ISK.alpha, amount, cardNumber, expiryDate);
// Authorization using card number, expiry date and card verification code
Authorization authorization = client.authorizeWithCVC(Currency.ISK.alpha, amount, cardNumber, expiryDate, cardVerificationCode);
// Authorization using token
Authorization authorization = client.authorizeWithToken(Currency.ISK.alpha, amount, token);
// Authorize and store card number information (token)
authorization = client.authorizeAndStoreToken(Currency.ISK.alpha, amount, cardNumber, expiryDate, token)
```

2. Authorization + Payment
```java
// Capture previously approved authorization
Authorization authorization = client.authorize(Currency.ISK.alpha, amount, cardNumber, expiryDate);
Payment payment = client.captureAuthorization(Currency.ISK.alpha, amount, authorization.getAuthorizationGuid());
```

3. Payment
```java
// Payment using card number and expiry date
Payment payment = client.payment(Currency.ISK.alpha, amount, cardNumber, expiryDate);
// Payment using card number, expiry date and card verification code
Payment payment = client.paymentWithCVC(Currency.ISK.alpha, amount, cardNumber, expiryDate, cardVerificationCode);
// Payment using token
Payment payment = client.paymentWithToken(Currency.ISK.alpha, amount, token);
// Payment and storing card number information (token)
Payment payment = client.paymentAndStoreToken(Currency.ISK.alpha, amount, carNumber, expiryDate, token);
```

4. Authorization + Cancellation
```java
try {
    Authorization authorization = client.authorize(Currency.ISK.alpha, amount, cardNumber, expiryDate);
catch(HpEcommerceException e) {
    Cancellation cancellation = client.cancelAuthorization(Currency.ISK.alpha, amount, e.terminalDateTime());
 }
```


5. Refund
Refund refund = client.refund(Currency.ISK.alpha, amount, cardNumber, expiryDate);

6. Reversal
```java
// Reverse authorization
Authorization authorization = client.authorize(Currency.ISK.alpha, amount, cardNumber, expiryDate);
Reversal reversal = client.reverseAuthorization(authorization.getAuthorizationGuid());
// Reverse Payment
Payment payment = client.payment(Currency.ISK.alpha, amount, cardNumber, expiryDate);
Reversal reversal = client.reversePayment(payment.getPaymentGuid());
// Reverse refund
Refund refund = client.refund(Currency.ISK.alpha, amount, cardNumber, expiryDate);
Reversal reversal = client.reverseRefund(refund.getRefundGuid());
```

7. Linked refund
```java
Payment payment = client.payment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
Refund refund = client.refundPayment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, payment.getPaymentGuid());
```

8. Get Token
```java
Token token = client.getToken(token);
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
Token token = client.deleteToken("token_identifier", cardNumber, expiryDate);
```






