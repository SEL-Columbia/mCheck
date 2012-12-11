package org.who.formhub.api.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class FormHubFormDefinition {
    private String name;
    private Map<String, String> mappings;

    public FormHubFormDefinition(String name, Map<String, String> mappings) {
        this.name = name;
        this.mappings = mappings;
    }

    public String name() {
        return name;
    }

    public String url(String baseURL, String previousToken) {
        return "http://" + baseURL + "/" + name + "/api?query=%7B%22_id%22%3A+%7B+%22%24gt%22%3A+" + previousToken+ "+%7D%7D";
    }

    public Map<String, String> mappings() {
        return mappings;
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
