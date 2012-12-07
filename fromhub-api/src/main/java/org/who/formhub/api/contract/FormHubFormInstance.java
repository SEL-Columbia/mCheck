package org.who.formhub.api.contract;

import java.util.HashMap;
import java.util.Map;

public class FormHubFormInstance {
    private String name;
    private Map<String, String> fieldsWeCareAbout;

    public FormHubFormInstance(FormHubFormDefinition definition, Map<String, String> formDatum) {
        name = definition.name();
        fieldsWeCareAbout = mapFieldsBasedOnDefinition(definition.mappings(), formDatum);
    }

    public String name() {
        return name;
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
