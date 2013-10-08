package org.who.mcheck.core.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'ReminderStatusToken'")
public class ReminderStatusToken extends MotechBaseDataObject {

    @JsonProperty
    private String contactNumber;
    @JsonProperty
    private ReminderStatus reminderStatus;
    @JsonProperty
    private String daySinceDelivery;
    @JsonProperty
    private int attemptNumber;

    private ReminderStatusToken() {
    }

    public ReminderStatusToken(String contactNumber, ReminderStatus reminderStatus) {
        this.contactNumber = contactNumber;
        this.reminderStatus = reminderStatus;
    }

    public void markCallStatusAsSuccessful() {
        this.reminderStatus = ReminderStatus.Successful;
    }

    //Do not rename, this is needed by Couch view
    public String getContactNumber() {
        return contactNumber;
    }

    public ReminderStatusToken withDaySinceDelivery(String daySinceDelivery) {
        this.daySinceDelivery = daySinceDelivery;
        return this;
    }

    public ReminderStatusToken withCallAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
        return this;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public ReminderStatus callStatus() {
        return reminderStatus;
    }

    public String daySinceDelivery() {
        return daySinceDelivery;
    }

    public int attemptNumber() {
        return attemptNumber;
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
