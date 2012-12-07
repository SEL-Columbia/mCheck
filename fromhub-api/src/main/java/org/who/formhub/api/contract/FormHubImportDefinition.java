package org.who.formhub.api.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class FormHubImportDefinition {
    private String userName;
    private String password;
    private String formHubBaseURL;
    private List<FormHubFormDefinition> forms;

    public String userName() {
        return userName;
    }

    public String password() {
        return password;
    }

    public String formHubBaseURL() {
        return formHubBaseURL;
    }

    public List<FormHubFormDefinition> forms() {
        return forms;
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
