package com.handpoint.ecommerce.messages.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "tokenStore")
@XmlAccessorType(XmlAccessType.FIELD)
public class Token {

    @XmlElement
    private String token;
    @XmlElement
    private String maskedCardNumber;
    @XmlElement
    private String expiryDateMMYY;
    @XmlElement
    private String cardTypeName;

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }
}
