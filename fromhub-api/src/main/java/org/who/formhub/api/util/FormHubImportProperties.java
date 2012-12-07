package org.who.formhub.api.util;

import org.motechproject.dao.MotechJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.who.formhub.api.contract.FormHubImportDefinition;

import java.util.Properties;

@Component
public class FormHubImportProperties {
    public static final String FORMHUB_IMPORT_DEFINITION_FILE = "formhub-import.definition.file";
    private final Properties properties;

    @Autowired
    public FormHubImportProperties(@Qualifier("propertiesForFormHubImport") Properties properties) {
        this.properties = properties;
    }

    public FormHubImportDefinition importDefinition() {
        String jsonPath = properties.getProperty(FORMHUB_IMPORT_DEFINITION_FILE);
        return (FormHubImportDefinition) new MotechJsonReader().readFromFile(jsonPath, FormHubImportDefinition.class);
    }
}
