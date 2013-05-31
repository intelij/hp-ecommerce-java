package com.handpoint.ecommerce.messages.payment;

import com.handpoint.ecommerce.core.Currency;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cancellation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CancellationRequest {

    @NotNull(message = "transactionType is required")
    @XmlElement
    private String transactionType;
    @XmlElement
    @NotNull(message = "currency is required")
    private String currency;
    @XmlElement
    private String amount;
    @XmlElement
    private String terminalDateTime;

    public CancellationRequest() {
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public String getTerminalDateTime() {
        return terminalDateTime;
    }

    public void setTerminalDateTime(String terminalDateTime) {
        this.terminalDateTime = terminalDateTime;
    }
}
