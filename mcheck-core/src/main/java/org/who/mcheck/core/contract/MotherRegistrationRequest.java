package org.who.mcheck.core.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MotherRegistrationRequest {
    private String formHubId;
    private String name;
    private String contactNumber;
    private String bleeding;
    private String submissionDate;

    public MotherRegistrationRequest(String formHubId, String name, String contactNumber, String bleeding, String submissionDate) {
        this.formHubId = formHubId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.bleeding = bleeding;
        this.submissionDate = submissionDate;
    }

    public String formHubId() {
        return formHubId;
    }

    public String name() {
        return name;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public String bleeding() {
        return bleeding;
    }

    public String submissionDate() {
        return submissionDate;
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
