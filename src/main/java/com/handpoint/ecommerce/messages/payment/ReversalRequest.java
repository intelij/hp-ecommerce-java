package com.handpoint.ecommerce.messages.payment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reversal")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReversalRequest {

    @XmlElement
    private String paymentGuid;
    @XmlElement
    private String authorizationGuid;
    @XmlElement
    private String refundGuid;
    @XmlElement
    private String customerReference;

    public ReversalRequest() {
    }

    public String getPaymentGuid() {
        return paymentGuid;
    }

    public void setPaymentGuid(String paymentGuid) {
        this.paymentGuid = paymentGuid;
    }

    public String getAuthorizationGuid() {
        return authorizationGuid;
    }

    public void setAuthorizationGuid(String authorizationGuid) {
        this.authorizationGuid = authorizationGuid;
    }

    public String getRefundGuid() {
        return refundGuid;
    }

    public void setRefundGuid(String refundGuid) {
        this.refundGuid = refundGuid;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }
}
