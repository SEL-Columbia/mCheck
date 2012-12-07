package org.who.formhub.api.contract;

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
        return "http://" + baseURL + "/" + name + "%22&format=json&previous_export=" + previousToken;
    }

    public Map<String, String> mappings() {
        return mappings;
    }
}
