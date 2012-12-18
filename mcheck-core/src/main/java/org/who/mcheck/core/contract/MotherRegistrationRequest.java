package org.who.mcheck.core.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MotherRegistrationRequest {
    private String formHubId;
    private String name;
    private String contactNumber;
    private String hasBleeding;
    private String hasFever;
    private String hasPainfulUrination;
    private String hasVaginalDischarge;
    private String hasHeadache;
    private String hasProblemBreathing;
    private String submissionDate;

    public MotherRegistrationRequest(String formHubId, String name, String contactNumber, String hasBleeding, String hasFever, String hasPainfulUrination, String hasVaginalDischarge, String hasHeadache, String hasProblemBreathing, String submissionDate) {
        this.formHubId = formHubId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.hasBleeding = hasBleeding;
        this.hasFever = hasFever;
        this.hasPainfulUrination = hasPainfulUrination;
        this.hasVaginalDischarge = hasVaginalDischarge;
        this.hasHeadache = hasHeadache;
        this.hasProblemBreathing = hasProblemBreathing;
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

    public String hasBleeding() {
        return hasBleeding;
    }

    public String hasFever() {
        return hasFever;
    }

    public String hasPainfulUrination() {
        return hasPainfulUrination;
    }

    public String hasVaginalDischarge() {
        return hasVaginalDischarge;
    }

    public String hasHeadache() {
        return hasHeadache;
    }

    public String hasProblemBreathing() {
        return hasProblemBreathing;
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
