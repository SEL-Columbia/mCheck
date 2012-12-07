package org.who.formhub.api.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'ExportToken'")
public class ExportToken extends MotechBaseDataObject {
    @JsonProperty
    private String formName;
    @JsonProperty
    private String value;

    public ExportToken() {
    }

    public ExportToken(String formName, String value) {
        this.formName = formName;
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String getFormName() {
        return formName;
    }
}
