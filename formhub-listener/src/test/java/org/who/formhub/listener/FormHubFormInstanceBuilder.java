package org.who.formhub.listener;

import org.who.formhub.api.contract.FormHubFormDefinition;
import org.who.formhub.api.contract.FormHubFormInstance;

import java.util.HashMap;
import java.util.Map;

public class FormHubFormInstanceBuilder {
    private String formName;
    private Map<String, String> mappings;
    private Map<String, String> content;
    private String handler;

    public FormHubFormInstanceBuilder() {
        this.mappings = new HashMap<>();
    }

    public FormHubFormInstanceBuilder withName(String formName) {
        this.formName = formName;
        return this;
    }

    public FormHubFormInstanceBuilder withMapping(String pathToField, String parameterToBeMappedTo) {
        mappings.put(pathToField, parameterToBeMappedTo);
        return this;
    }

    public FormHubFormInstanceBuilder withContent(Map<String, String> content) {
        this.content = content;
        return this;
    }

    public FormHubFormInstance build() {
        return new FormHubFormInstance(new FormHubFormDefinition(formName, handler, mappings), content);
    }

    public FormHubFormInstanceBuilder withHandler(String handler) {
        this.handler = handler;
        return this;
    }
}
