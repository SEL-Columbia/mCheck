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
    private String bleeding;
    @JsonProperty
    private String registrationDate;

    private Mother(){
    }

    public Mother(String formHubID, String name, String contactNumber, String bleeding, String registrationDate) {
        this.formHubID = formHubID;
        this.name = name;
        this.contactNumber = contactNumber;
        this.bleeding = bleeding;
        this.registrationDate = registrationDate;
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

    public String name() {
        return name;
    }

    public String formHubID() {
        return formHubID;
    }

    public String contactNumber() {
        return contactNumber;
    }

    public String bleeding() {
        return bleeding;
    }
}
