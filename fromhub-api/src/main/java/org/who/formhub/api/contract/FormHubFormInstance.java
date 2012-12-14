package org.who.formhub.api.contract;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FormHubFormInstance implements Serializable {
    private static final long serialVersionUID = 42L;

    private String handler;
    private Map<String, String> fieldsWeCareAbout;

    public FormHubFormInstance(FormHubFormDefinition definition, Map<String, String> formDatum) {
        handler = definition.handler();
        fieldsWeCareAbout = mapFieldsBasedOnDefinition(definition.mappings(), formDatum);
    }

    public String handler() {
        return handler;
    }

    public Map<String, String> fields() {
        return fieldsWeCareAbout;
    }

    private Map<String, String> mapFieldsBasedOnDefinition(Map<String, String> mappings, Map<String, String> formDatum) {
        Map<String, String> fields = new HashMap<>();
        for (String fieldName : mappings.keySet()) {
            if (formDatum.containsKey(fieldName)) {
                String value = formDatum.get(fieldName);
                fields.put(mappings.get(fieldName), value == null ? "" : value);
            }
        }
        return fields;
    }
}
