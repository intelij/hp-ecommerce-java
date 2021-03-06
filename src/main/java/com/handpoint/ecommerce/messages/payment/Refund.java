package com.handpoint.ecommerce.messages.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "refund")
@XmlAccessorType(XmlAccessType.FIELD)
public class Refund {

    @XmlElement
    private String refundGuid;
    @XmlElement()
    private String paymentGuid;
    @XmlElement
    private String amount;
    @XmlElement
    private String currency;
    @XmlElement
    private String cardTypeName;
    @XmlElement
    private String maskedCardNumber;
    @XmlElement
    private String expiryDateMMYY;
    @XmlElement
    private String customerReference;
    @XmlElement
    private String approvalCode;
    @XmlElement
    private String issuerResponseText;
    @XmlElement
    private String serverDateTime;
    @XmlElement
    private String terminalDateTime;
    @XmlElement
    private String agreementNumber;
    @XmlElement
    private String cardAcceptorName;
    @XmlElement
    private String cardAcceptorAddress;

    public Refund() {
    }

    public String getRefundGuid() {
        return refundGuid;
    }

    public void setRefundGuid(String refundGuid) {
        this.refundGuid = refundGuid;
    }

    public String getPaymentGuid() {
        return paymentGuid;
    }

    public void setPaymentGuid(String paymentGuid) {
        this.paymentGuid = paymentGuid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getExpiryDateMMYY() {
        return expiryDateMMYY;
    }

    public void setExpiryDateMMYY(String expiryDateMMYY) {
        this.expiryDateMMYY = expiryDateMMYY;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getIssuerResponseText() {
        return issuerResponseText;
    }

    public void setIssuerResponseText(String issuerResponseText) {
        this.issuerResponseText = issuerResponseText;
    }

    public String getServerDateTime() {
        return serverDateTime;
    }

    public void setServerDateTime(String serverDateTime) {
        this.serverDateTime = serverDateTime;
    }

    public String getTerminalDateTime() {
        return terminalDateTime;
    }

    public void setTerminalDateTime(String terminalDateTime) {
        this.terminalDateTime = terminalDateTime;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public String getCardAcceptorName() {
        return cardAcceptorName;
    }

    public void setCardAcceptorName(String cardAcceptorName) {
        this.cardAcceptorName = cardAcceptorName;
    }

    public String getCardAcceptorAddress() {
        return cardAcceptorAddress;
    }

    public void setCardAcceptorAddress(String cardAcceptorAddress) {
        this.cardAcceptorAddress = cardAcceptorAddress;
    }
}
