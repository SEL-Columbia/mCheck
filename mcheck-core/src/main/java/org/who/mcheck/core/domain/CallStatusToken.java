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

    private CallStatusToken() {
    }

    public CallStatusToken(String contactNumber, CallStatus callStatus) {
        this.contactNumber = contactNumber;
        this.callStatus = callStatus;
    }

    public void markCallStatusAsSuccessful() {
        this.callStatus = CallStatus.Successful;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
