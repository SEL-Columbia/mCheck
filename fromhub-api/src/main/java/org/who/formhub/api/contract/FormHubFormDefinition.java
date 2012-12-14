package org.who.formhub.api.contract;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class FormHubFormDefinition {
    private String name;
    private String handler;
    private Map<String, String> mappings;

    public FormHubFormDefinition(String name, Map<String, String> mappings) {
        this.name = name;
        this.mappings = mappings;
    }

    public String name() {
        return name;
    }

    public Map<String, String> mappings() {
        return mappings;
    }

    public String handler() {
        return handler;
    }

    public String url(String baseURL, String username, String previousToken) {
        String token = StringUtils.isBlank(previousToken) ? "0" : previousToken;
        return baseURL + "/" + username + "/forms/" + name + "/api?query=%7B%22_id%22%3A+%7B%22%24gt%22+%3A+" + token+ "%7D%7D";
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
