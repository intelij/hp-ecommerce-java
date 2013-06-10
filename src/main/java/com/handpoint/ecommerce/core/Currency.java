package com.handpoint.ecommerce.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * The country code enum based on the ISO-4217 standard.
 * <p/>
 * The values used  here were obtained from wikipedia (http://en.wikipedia.org/wiki/ISO_4217). A few currencies
 * from that list are omitted (commodities like gold, silver, platinum) including the two non-decimal currencies
 * (ariary of Mgdagaskar and Mauritanian ouguiya).
 * <p/>
 * Note: Possible future enhancements (if they are of any value) would be a link to the CountryEnum indicating
 * in what countries the currency is in circulation. This info is available from the above wikipedia page.
 */
@XmlType(name = "currency")
@XmlEnum
public enum Currency {


    @XmlEnumValue("CAD")
    CAD(124, "CAD", "Canadian dollar"),

    @XmlEnumValue("CHF")
    CHF(756, "CHF", "Swiss franc"),

    @XmlEnumValue("CNY")
    CNY(156, "CNY", "Chinese Yuan"),

    @XmlEnumValue("CZK")
    CZK(203, "CZK", "Czech Koruna"),

    @XmlEnumValue("DKK")
    DKK(208, "DKK", "Danish krone"),

    @XmlEnumValue("EUR")
    EUR(978, "EUR", "Euro"),

    @XmlEnumValue("GBP")
    GBP(826, "GBP", "Pound sterling"),

    @XmlEnumValue("ISK")
    ISK(352, "ISK", "Iceland krona"),

    @XmlEnumValue("NOK")
    NOK(578, "NOK", "Norwegian krone"),

    @XmlEnumValue("rub")
    RUB(643, "RUB", "Russian rouble"),

    @XmlEnumValue("SEK")
    SEK(752, "SEK", "Swedish krona/kronor"),

    @XmlEnumValue("SGD")
    SGD(702, "SGD", "Singapore dollar"),

    @XmlEnumValue("USD")
    USD(840, "USD", "US dollar");

    public final int id;
    public final String alpha;
    public final String label;

    private Currency(int id, String alpha, String label) {
        this.id = id;
        this.alpha = alpha;
        this.label = label;
    }

    public static Currency fromValue(String v) {
        for (Currency c : Currency.values()) {
            if (c.alpha.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
