package org.who.formhub.api.contract;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.who.formhub.api.util.EasyMap.mapOf;

public class FormHubFormDefinitionTest {

    @Test
    public void shouldCreateUrlWithPreviousTokenAsItsNotEmpty() throws Exception {
        FormHubFormDefinition formDefinition = new FormHubFormDefinition("formName", "handler", mapOf("key", "value"));

        String url = formDefinition.url("http://www.server-name.org", "userName", "token");

        assertEquals("http://www.server-name.org/userName/forms/formName/api?query=%7B%22_id%22%3A+%7B%22%24gt%22+%3A+" + "token" + "%7D%7D", url);
    }

    @Test
    public void shouldCreateUrlWithPreviousTokenAsZeroWhenItsEmpty() throws Exception {
        FormHubFormDefinition formDefinition = new FormHubFormDefinition("formName", "handler", mapOf("key", "value"));

        String url = formDefinition.url("http://www.server-name.org", "userName", "");

        assertEquals("http://www.server-name.org/userName/forms/formName/api?query=%7B%22_id%22%3A+%7B%22%24gt%22+%3A+" + "0" + "%7D%7D", url);
    }

    @Test
    public void shouldCreateUrlWithPreviousTokenAsZeroWhenItsNull() throws Exception {
        FormHubFormDefinition formDefinition = new FormHubFormDefinition("formName", "handler", mapOf("key", "value"));

        String url = formDefinition.url("http://www.server-name.org", "userName", null);

        assertEquals("http://www.server-name.org/userName/forms/formName/api?query=%7B%22_id%22%3A+%7B%22%24gt%22+%3A+" + "0" + "%7D%7D", url);
    }

}
