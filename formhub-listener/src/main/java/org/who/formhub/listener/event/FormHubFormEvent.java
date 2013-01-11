package org.who.formhub.listener.event;

import org.motechproject.event.MotechEvent;
import org.who.formhub.api.contract.FormHubFormInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormHubFormEvent {

    public static final String EVENT_SUBJECT = "FORMS_EVENT";
    public static final String FORM_INSTANCES_PARAMETER = "FormInstances";

    private List<FormHubFormInstance> formInstances;

    public FormHubFormEvent(List<FormHubFormInstance> formInstances) {
        this.formInstances = formInstances;
    }

    public MotechEvent toMotechEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(FORM_INSTANCES_PARAMETER, formInstances);
        return new MotechEvent(EVENT_SUBJECT, parameters);
    }
}
