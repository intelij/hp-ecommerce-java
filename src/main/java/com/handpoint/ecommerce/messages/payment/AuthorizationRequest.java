package com.handpoint.ecommerce.messages.payment;



import com.handpoint.ecommerce.core.Currency;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.validation.constraints.NotNull;


/**
 * XML Object for Authorization Request
 */
@XmlRootElement(name = "authorization")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthorizationRequest {

    @XmlElement
    @NotNull(message = "paymentScenario is required")
    private String paymentScenario;

    @XmlElement
    @NotNull(message = "currency is required")
    private String currency;

    @XmlElement
    @NotNull(message = "amount is required")
    private String amount;

    @XmlElement
    private String token;

    @XmlElement
    private String cardNumber;

    @XmlElement
    @Size(min = 4, max = 4, message = "expiryDateMMYY must be four characters")
    private String expiryDateMMYY;

    @XmlElement
    private String cardVerificationCode;

    @XmlElement
    private String customerReference;

    public String getPaymentScenario() {
        return paymentScenario;
    }

    public void setPaymentScenario(String paymentScenario) {
        this.paymentScenario = paymentScenario;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDateMMYY() {
        return expiryDateMMYY;
    }

    public void setExpiryDateMMYY(String expiryDateMMYY) {
        this.expiryDateMMYY = expiryDateMMYY;
    }

    public String getCardVerificationCode() {
        return cardVerificationCode;
    }

    public void setCardVerificationCode(String cardVerificationCode) {
        this.cardVerificationCode = cardVerificationCode;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }
}
