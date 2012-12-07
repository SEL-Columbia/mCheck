package org.who.formhub.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.who.formhub.api.contract.FormHubFormDefinition;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.contract.FormHubHttpResponse;
import org.who.formhub.api.repository.AllExportTokens;
import org.who.formhub.api.util.FormHubHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class FormHubFormImportService {
    public static final String UPDATE_TOKEN_FIELD = "_id";
    private FormHubHttpClient httpClient;
    private AllExportTokens allExportTokens;

    @Autowired
    public FormHubFormImportService(FormHubHttpClient httpClient, AllExportTokens allExportTokens) {
        this.httpClient = httpClient;
        this.allExportTokens = allExportTokens;
    }

    public List<FormHubFormInstance> fetchForms(List<FormHubFormDefinition> formDefinitions, String baseUrl, String username, String password) {
        return processAllForms(fetchAllForms(formDefinitions, baseUrl, username, password));
    }

    private List<FormHubFormWithResponse> fetchAllForms(List<FormHubFormDefinition> formDefinitions, String baseUrl, String username, String password) {
        List<FormHubFormWithResponse> formWithResponses = new ArrayList<>();
        for (FormHubFormDefinition formDefinition : formDefinitions) {

            String oldToken = allExportTokens.findByFormName(formDefinition.name()).value();
            FormHubHttpResponse formHubHttpResponse = httpClient.get(formDefinition.url(baseUrl, oldToken), baseUrl, username, password);

            if (formHubHttpResponse.isValid()) {
                formWithResponses.add(new FormHubFormWithResponse(formDefinition, formHubHttpResponse));
            }
        }
        return formWithResponses;
    }

    private List<FormHubFormInstance> processAllForms(List<FormHubFormWithResponse> formWithResponses) {
        List<FormHubFormInstance> formInstances = new ArrayList<>();
        for (FormHubFormWithResponse formWithResponse : formWithResponses) {
            FormHubFormDefinition definition = formWithResponse.formDefinition;
            FormHubHttpResponse response = formWithResponse.response;

            List<Map<String, String>> formData;
            try {
                formData = new Gson().fromJson(response.contentAsString(), new TypeToken<List<Map<String, String>>>() {
                }.getType());
            } catch (JsonParseException e) {
                throw new RuntimeException(response.contentAsString() + e);
            }
            for (Map<String, String> formDatum : formData) {
                formInstances.add(new FormHubFormInstance(definition, formDatum));
                updateToken(definition.name(), formDatum.get(UPDATE_TOKEN_FIELD));
            }
        }
        return formInstances;
    }

    private void updateToken(String formName, String token) {
        if (isBlank(token)) {
            return;
        }
        allExportTokens.updateToken(formName, token);
    }

    private class FormHubFormWithResponse {
        private final FormHubFormDefinition formDefinition;
        private final FormHubHttpResponse response;

        private FormHubFormWithResponse(FormHubFormDefinition formDefinition, FormHubHttpResponse response) {
            this.formDefinition = formDefinition;
            this.response = response;
        }
    }
}
