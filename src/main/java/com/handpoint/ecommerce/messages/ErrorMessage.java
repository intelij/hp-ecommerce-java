package com.handpoint.ecommerce.messages;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * If the error returns an error message, this is the object its mapped to.
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorMessage {

    @XmlElement
    private String reason;

    @XmlElementWrapper(name = "details")
    private List<String> detail;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getDetails() {
        return detail;
    }

    public void setDetails(List<String> details) {
        this.detail = details;
    }
}
