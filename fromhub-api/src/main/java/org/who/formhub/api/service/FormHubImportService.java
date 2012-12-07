package org.who.formhub.api.service;

import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.contract.FormHubImportDefinition;
import org.who.formhub.api.util.FormHubImportProperties;

import java.util.List;

public class FormHubImportService {
    private FormHubImportProperties formHubImportProperties;
    private FormHubFormImportService formHubFormImportService;

    public FormHubImportService(FormHubImportProperties formHubImportProperties, FormHubFormImportService formHubFormImportService) {
        this.formHubImportProperties = formHubImportProperties;
        this.formHubFormImportService = formHubFormImportService;
    }

    public List<FormHubFormInstance> fetchForms() {
        FormHubImportDefinition importDefinition = formHubImportProperties.importDefinition();
        return formHubFormImportService.fetchForms(importDefinition.forms(), importDefinition.formHubBaseURL(), importDefinition.userName(), importDefinition.password());
    }
}
