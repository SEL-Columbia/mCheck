package org.who.formhub.api.service;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.who.formhub.api.contract.FormHubFormDefinition;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.contract.FormHubHttpResponse;
import org.who.formhub.api.domain.ExportToken;
import org.who.formhub.api.repository.AllExportTokens;
import org.who.formhub.api.util.FormHubHttpClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.who.formhub.api.util.EasyMap.create;

public class FormHubFormImportServiceTest {
    @Mock
    private FormHubHttpClient httpClient;
    @Mock
    private AllExportTokens allExportTokens;

    private FormHubFormImportService service;
    private String baseURL = "www.formhub.org";
    private String formName = "mCheck";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new FormHubFormImportService(httpClient, allExportTokens);
    }

    @Test
    public void shouldFetchFormsFromFormHub() throws Exception {
        String exportURL = "http://" + baseURL + "/" + formName + "%22&format=json&previous_export=";
        FormHubFormDefinition formDefinition = setUpForm(formName, 200, "/testData/form.1.dump.json", "");

        List<FormHubFormInstance> formInstances = service.fetchForms(asList(formDefinition), baseURL, "username", "password");

        verify(httpClient).get(exportURL, baseURL, "username", "password");
        assertEquals(2, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"Lata", "965326894"}, formName);
        assertForm(formInstances.get(1), new String[]{"Asha", "757885443"}, formName);
    }

    @Test
    public void shouldConvertNullValuesInFormHubFormExportAsEmptyStrings() throws Exception {
        String exportURL = "http://" + baseURL + "/" + formName + "%22&format=json&previous_export=";
        FormHubFormDefinition formDefinition = setUpForm(formName, 200, "/testData/form.2.dump.json", "");

        List<FormHubFormInstance> formInstances = service.fetchForms(asList(formDefinition), baseURL, "username", "password");

        verify(httpClient).get(exportURL, baseURL, "username", "password");
        assertEquals(2, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"Lata", ""}, formName);
        assertForm(formInstances.get(1), new String[]{"", "757885443"}, formName);
    }

    @Test
    public void shouldFetchMultipleFormsWithMultipleInstancesFromFormHub() throws Exception {
        String firstFormName = "mCheck1";
        String secondFormName = "mCheck2";
        String urlOfFirstExport = "http://" + baseURL + "/" + firstFormName + "%22&format=json&previous_export=OLD-TOKEN";
        String urlOfSecondExport = "http://" + baseURL + "/" + secondFormName + "%22&format=json&previous_export=";

        FormHubFormDefinition formDefinition = setUpForm(firstFormName, 200, "/testData/form.1.dump.json", "OLD-TOKEN");
        FormHubFormDefinition secondFormDefinition = setUpForm(secondFormName, 200, "/testData/form.2.dump.json", "");

        List<FormHubFormInstance> formInstances = service.fetchForms(asList(formDefinition,secondFormDefinition), baseURL, "username", "password");

        InOrder inOrder = inOrder(httpClient, allExportTokens);
        inOrder.verify(httpClient).get(urlOfFirstExport, baseURL, "username", "password");
        inOrder.verify(httpClient).get(urlOfSecondExport, baseURL, "username", "password");
        inOrder.verify(allExportTokens).updateToken(firstFormName, "1");
        inOrder.verify(allExportTokens).updateToken(firstFormName, "2");
        inOrder.verify(allExportTokens).updateToken(secondFormName, "34561");
        inOrder.verify(allExportTokens).updateToken(secondFormName, "34562");

        assertEquals(4, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"Lata", "965326894"}, firstFormName);
        assertForm(formInstances.get(1), new String[]{"Asha", "757885443"}, firstFormName);
        assertForm(formInstances.get(2), new String[]{"Lata", ""}, secondFormName);
        assertForm(formInstances.get(3), new String[]{"", "757885443"}, secondFormName);
    }

    @Test
    public void shouldNotUpdateTokenIfTokenFieldIsEmptyOrNull() throws Exception {
        String formName = "mCheck";
        FormHubFormDefinition formDefinition = setUpForm(formName, 200, "/testData/form.3.dump.json", "OLD-TOKEN");

        service.fetchForms(asList(formDefinition), baseURL, "username", "password");

        verify(allExportTokens, times(1)).updateToken(formName,"1");
    }

    @Test
    public void shouldUseURLWithoutPreviousTokenWhenThereIsNoToken() throws Exception {
        String formName = "mCheck";
        String exportURL = "http://" + baseURL + "/" + formName + "%22&format=json&previous_export=";
        FormHubFormDefinition formDefinition = setUpForm(formName, 200, "/testData/form.3.dump.json", "");

        service.fetchForms(asList(formDefinition), baseURL, "username", "password");

        verify(httpClient).get(exportURL, baseURL, "username", "password");
    }

    @Test
    public void shouldNotProcessFormOrUpdateTokenWhenResponseSaysThatThereIsNoNewData() throws Exception {
        String formName = "mCheck";
        FormHubFormDefinition formDefinition = setUpForm(formName, 200, "/testData/form.4.dump.json", "");

        List<FormHubFormInstance> formInstances = service.fetchForms(asList(formDefinition), baseURL, "username", "password");

        assertThat(formInstances.size(), is(0));
        verify(allExportTokens,times(0)).updateToken(anyString(),anyString());
    }

    @Test
    public void shouldNotProcessFormOrUpdateTokenWhenResponseStatusCodeIsNotValid() throws Exception {
        String firstFormName = "mCheck1";
        String secondFormName = "mCheck2";
        FormHubFormDefinition firstFormDefinition = setUpForm(firstFormName, 404, "/testData/form.1.dump.json", "");
        FormHubFormDefinition secondFormDefinition = setUpForm(secondFormName, 200, "/testData/form.1.dump.json", "");

        List<FormHubFormInstance> formInstances = service.fetchForms(asList(firstFormDefinition, secondFormDefinition), baseURL, "username", "password");

        assertThat(formInstances.size(), is(2));
        verify(allExportTokens).updateToken(secondFormName, "1");
        verify(allExportTokens).updateToken(secondFormName, "2");
        verify(allExportTokens, times(2)).updateToken(anyString(),anyString());
    }

    private FormHubFormDefinition setUpForm(String formName, int statusCode, String jsonPath, String oldToken) throws IOException {
        FormHubFormDefinition formDefinition = new FormHubFormDefinition(formName, create("mother_name", "FieldInOutput").put("mother_number", "AnotherFieldInOutput").map());
        FormHubHttpResponse httpResponse = new FormHubHttpResponse(statusCode, IOUtils.toByteArray(getClass().getResourceAsStream(jsonPath)));
        when(allExportTokens.findByFormName(formName)).thenReturn(new ExportToken(formName, oldToken));
        when(httpClient.get(formDefinition.url(baseURL, oldToken), baseURL, "username", "password")).thenReturn(httpResponse);
        return formDefinition;
    }

    private void assertForm(FormHubFormInstance actualFormInstance, String[] expectedValuesOfForm, String formName) {
        assertEquals(actualFormInstance.name(), formName);

        Map<String, String> data = actualFormInstance.fields();

        assertEquals(2, data.size());
        assertThat(data.get("FieldInOutput"), is(expectedValuesOfForm[0]));
        assertThat(data.get("AnotherFieldInOutput"), is(expectedValuesOfForm[1]));
    }
}
