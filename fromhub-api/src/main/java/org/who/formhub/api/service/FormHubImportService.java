package org.who.formhub.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.contract.FormHubImportDefinition;
import org.who.formhub.api.util.FormHubImportProperties;

import java.util.List;

@Service
public class FormHubImportService {
    private FormHubImportProperties formHubImportProperties;
    private FormHubFormImportService formHubFormImportService;

    @Autowired
    public FormHubImportService(FormHubImportProperties formHubImportProperties, FormHubFormImportService formHubFormImportService) {
        this.formHubImportProperties = formHubImportProperties;
        this.formHubFormImportService = formHubFormImportService;
    }

    public List<FormHubFormInstance> fetchForms() {
        FormHubImportDefinition importDefinition = formHubImportProperties.importDefinition();
        return formHubFormImportService.fetchForms(importDefinition.forms(), importDefinition.formHubBaseURL(), importDefinition.userName(), importDefinition.password());
    }
}
