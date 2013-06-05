package com.handpoint.ecommerce.messages.token;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "tokenStore")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenRequest {

    @XmlElement
    @NotNull(message = "cardNumber is required")
    private String cardNumber;

    @XmlElement
    @NotNull(message = "expiryDateMMYY is required")
    @Size(min = 4, max = 4, message = "expiryDateMMYY must be four characters")
    private String expiryDateMMYY;

    public TokenRequest() {
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
}
