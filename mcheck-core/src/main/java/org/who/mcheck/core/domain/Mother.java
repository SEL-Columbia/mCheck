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
    private String contactNumber;
    @JsonProperty
    private String hasBleeding;
    @JsonProperty
    private String hasFever;
    @JsonProperty
    private String hasVaginalDischarge;
    @JsonProperty
    private String hasProblemBreathing;
    @JsonProperty
    private String hasPainfulUrination;
    @JsonProperty
    private String hasHeadache;
    @JsonProperty
    private String registrationDate;

    private Mother() {
    }

    public Mother(String formHubID, String name, String contactNumber, String hasBleeding, String hasFever, String hasPainfulUrination, String hasVaginalDischarge, String hasHeadache, String hasProblemBreathing, String registrationDate) {
        this.formHubID = formHubID;
        this.name = name;
        this.contactNumber = contactNumber;
        this.hasBleeding = hasBleeding;
        this.hasFever = hasFever;
        this.hasPainfulUrination = hasPainfulUrination;
        this.hasVaginalDischarge = hasVaginalDischarge;
        this.hasHeadache = hasHeadache;
        this.hasProblemBreathing = hasProblemBreathing;
        this.registrationDate = registrationDate;
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

    public String hasBleeding() {
        return hasBleeding;
    }

    public String hasFever() {
        return hasFever;
    }

    public String hasVaginalDischarge() {
        return hasVaginalDischarge;
    }

    public String hasProblemBreathing() {
        return hasProblemBreathing;
    }

    public String hasPainfulUrination() {
        return hasPainfulUrination;
    }

    public String hasHeadache() {
        return hasHeadache;
    }

    public String registrationDate() {
        return registrationDate;
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
