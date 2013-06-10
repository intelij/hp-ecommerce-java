# Handpoint E-commerce Java Client
=================

## Introduction
This is a java client library to integrate with Handpoint E-Commerce API.

## Installation
For Maven, add the following dependency to your POM:
```xml
<dependency>
    <groupId>com.handpoint</groupId>
    <artifactId>ecommerce-client-java</artifactId>
    <version>1.0.1</version>
</dependency>
```
If you are not using maven you can download it and its dependencies manually and include it in your project

1. [ecommerce-client-java-1.0.1-javadoc.jar](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.1/ecommerce-client-java-1.0.1-javadoc.jar)
2. [ecommerce-client-java-1.0.1-sources.jar](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.1/ecommerce-client-java-1.0.1-sources.jar)
3. [ecommerce-client-java-1.0.1](https://oss.sonatype.org/content/groups/public/com/handpoint/ecommerce-client-java/1.0.1/ecommerce-client-java-1.0.1.jar)
4. [hibernate-validator](http://repo1.maven.org/maven2/org/hibernate/hibernate-validator/4.3.1.Final/hibernate-validator-4.3.1.Final.jar)
5. [jersey-client](http://repo1.maven.org/maven2/com/sun/jersey/jersey-client/1.8/jersey-client-1.8.bundle)

## Usage
Here below are examples on how to initialize client and use for different kind of operations. .
Please take a look at ECommerceClientIT for more information [EcommerceClientIT](https://github.com/handpoint/hp-ecommerce-java/blob/master/src/test/java/com/handpoint/ecommerce/core/ECommerceClientIT.java)

### Initialize ECommerce Client
Instance ECommerceClient with values provided by Handpoint, for test environment, use the following values:

```java
public static final String CARD_ACCEPTOR = "7f6451e8314defbb50d0";
public static final String SHARED_SECRET = "8F10C8AD35B7AEC11675B50DBF6ACEAA0B4EC280B92500E51A02F7BBBE7B07C6";

// Instance client to test Environment without Http logging filter.
ECommerceClient client = new ECommerceClient(CARD_ACCEPTOR, SHARED_SECRET, Environment.TEST);

// Instance client to test Environment including logging filter.
ECommerceClient client = new ECommerceClient(CARD_ACCEPTOR, SHARED_SECRET, Environment.TEST, Boolean.TRUE);
```
### Operations
Note that all functions use the ECommerce client. See how it is initialized above.
If no exception is thrown from the client, it means it got a valid response from the Handpoint ECommerce web service. Response is valid if it has reached the acquirer, and it can either be declined or approved. If an approval code is set it is approved, otherwise not.
If an error occurs, there are three different kind of exceptions thrown. Exceptions are described below.
In the examples below, variables are used to pass in to the client. Example values:

```java
String amount = "70";
String cardNumber = "4222222222222";
String expiryDate = "1215";
String token = "TOKEN_1";
String cardVerificationCode = "123";
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

// Check if authorization is approved.
authorization = client.authorizeAndStoreToken(currency, amount, cardNumber, expiryDate, token)
if(authorization.getApprovalCode() != null) {
    // Authorization is approved
} else {
    // Authorization is declined.
}
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

### Exceptions
The client throws three different kind of errors, one for internal errors, one if message fails validation and one for server errors.

1. HpServerError
If a server error occurs a HpServerError is thrown. The exception includes a variable error message which includes the message the server returned. The error message includes a reason and a list of details, both are in human readable formats. Here is an example how to work with HpServerError

2. HpEcommerceException
If server does not respond due to network errors or other error, HpEcommerceException is thrown. This error includes a terminalDateTime since there is no way to know if the request was approved or not.

3. InvalidMessageException
If the request does not include all required data, InvalidMessageException is thrown. The message that comes with this exception is a human readable message describing the reasons why it failed validation.

```java
        try {
            authorization = client.authorize(Currency.ISK.alpha, AMOUNT_120_DECLINE_AMOUNT, AMERICAN_EXPRESS_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        } catch(HpServerError e){
            // Prints out the reason why the server error occurred. For example CardAcceptor not found.
            System.out.println(e.getErrorMessage().getReason());
        } catch(HpECommerceException e) {
            // Prints out the reason why an internal error occurred or if no answer is retrieved from the server.
            System.out.println(e.getMessage());
            // Prints out the date time the message was sent. It is recommended to try to cancel all authorizations that throw this exception,
            // since it might have been authorized but server could not respond due to network errors for example.
            System.out.println(e.terminalDateTime);
        } catch(InvalidMessageException e) {
            // Prints out which fields are missing in the request. For example: "currency is required"
            System.out.println(e.getMessage());
        }
```






