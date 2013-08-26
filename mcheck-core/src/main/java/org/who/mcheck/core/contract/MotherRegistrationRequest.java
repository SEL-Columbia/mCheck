package org.who.mcheck.core.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MotherRegistrationRequest {
    private String formHubID;
    private String name;
    private String husbandName;
    private String caseId;
    private String dateOfDelivery;
    private String registrationDate;
    private String contactNumber;
    private String dailyCallPreference;
    private String instanceID;
    private String submissionDate;

    public MotherRegistrationRequest(String formHubID, String name, String husbandName, String caseId, String dateOfDelivery, String registrationDate, String contactNumber, String dailyCallPreference, String instanceID, String submissionDate) {
        this.formHubID = formHubID;
        this.name = name;
        this.husbandName = husbandName;
        this.caseId = caseId;
        this.dateOfDelivery = dateOfDelivery;
        this.registrationDate = registrationDate;
        this.contactNumber = contactNumber;
        this.dailyCallPreference = dailyCallPreference;
        this.instanceID = instanceID;
        this.submissionDate = submissionDate;
    }

    public String formHubId() {
        return formHubID;
    }

    public String name() {
        return name;
    }

    public String husbandName() {
        return husbandName;
    }

    public String caseId() {
        return caseId;
    }

    public String dateOfDelivery() {
        return dateOfDelivery;
    }

    public String registrationDate() {
        return registrationDate;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public String dailyCallPreference() {
        return dailyCallPreference;
    }

    public String instanceID() {
        return instanceID;
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
