package org.who.mcheck.core.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'CallStatusToken'")
public class CallStatusToken extends MotechBaseDataObject {

    @JsonProperty
    private String contactNumber;
    @JsonProperty
    private CallStatus callStatus;
    private String daySinceDelivery;
    private int attemptNumber;

    private CallStatusToken() {
    }

    public CallStatusToken(String contactNumber, CallStatus callStatus) {
        this.contactNumber = contactNumber;
        this.callStatus = callStatus;
    }

    public void markCallStatusAsSuccessful() {
        this.callStatus = CallStatus.Successful;
    }

    //Do not rename, this is needed by Couch view
    public String getContactNumber() {
        return contactNumber;
    }

    public CallStatusToken withDaySinceDelivery(String daySinceDelivery) {
        this.daySinceDelivery = daySinceDelivery;
        return this;
    }

    public CallStatusToken withCallAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
        return this;
    }

    public String contactNumber() {
        return contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, new String[]{"id", "revision"});
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[]{"id", "revision"});
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
