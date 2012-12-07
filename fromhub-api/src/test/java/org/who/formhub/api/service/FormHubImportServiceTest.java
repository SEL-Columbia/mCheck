package org.who.formhub.api.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.who.formhub.api.contract.FormHubFormDefinition;
import org.who.formhub.api.util.FormHubImportProperties;

import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.who.formhub.api.util.FormHubImportProperties.FORMHUB_IMPORT_DEFINITION_FILE;

public class FormHubImportServiceTest {
    @Mock
    private FormHubFormImportService formImportService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldFetchAllForms() throws Exception {
        FormHubImportProperties commCareImportProperties = properties("/testData/formhub_export.json");
        List<FormHubFormDefinition> formDefinitions = commCareImportProperties.importDefinition().forms();

        new FormHubImportService(commCareImportProperties, formImportService).fetchForms();

        verify(formImportService).fetchForms(formDefinitions,"www.server.org" ,"someUser", "somePassword");
    }

    private FormHubImportProperties properties(String fileName) {
        Properties properties = new Properties();
        properties.setProperty(FORMHUB_IMPORT_DEFINITION_FILE, fileName);
        return new FormHubImportProperties(properties);
    }
}
