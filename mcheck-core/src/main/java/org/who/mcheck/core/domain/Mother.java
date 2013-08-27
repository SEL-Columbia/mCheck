package org.who.mcheck.core.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'Mother'")
public class Mother extends MotechBaseDataObject {

    @JsonProperty
    private String formHubID;
    @JsonProperty
    private String name;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String caseId;
    @JsonProperty
    private String dateOfDelivery;
    @JsonProperty
    private String registrationDate;
    @JsonProperty
    private String contactNumber;
    @JsonProperty
    private String dailyCallPreference;
    @JsonProperty
    private String instanceID;
    @JsonProperty
    private String submissionDate;

    private Mother() {
    }

    public Mother(String formHubID, String name, String husbandName, String caseId, String dateOfDelivery,
                  String registrationDate, String contactNumber, String dailyCallPreference, String instanceID,
                  String submissionDate) {
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

    public String name() {
        return name;
    }

    public String formHubID() {
        return formHubID;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public String registrationDate() {
        return registrationDate;
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
