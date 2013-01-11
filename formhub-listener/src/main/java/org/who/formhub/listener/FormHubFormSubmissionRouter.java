package org.who.formhub.listener;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.listener.event.FormHubFormEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.who.formhub.listener.event.FormHubFormEvent.EVENT_SUBJECT;

@Component
public class FormHubFormSubmissionRouter {
    private Object routeEventsHere;
    private static Logger logger = LoggerFactory.getLogger(FormHubFormSubmissionRouter.class);

    public void registerForDispatch(Object dispatchToThisObject) {
        this.routeEventsHere = dispatchToThisObject;
    }

    @MotechListener(subjects = EVENT_SUBJECT)
    public void handle(MotechEvent event) throws Exception {
        if (routeEventsHere == null) {
            logger.warn("No one is registered to whom events can be dispatched.");
            return;
        }

        Gson gson = new Gson();
        FormDispatchFailedException exception = new FormDispatchFailedException();
        List<FormHubFormInstance> formInstances = (List<FormHubFormInstance>) event.getParameters().get(FormHubFormEvent.FORM_INSTANCES_PARAMETER);
        for (FormHubFormInstance formInstance : formInstances) {
            String handler = formInstance.handler();
            String parameterJson = gson.toJson(formInstance.fields());
            try {
                dispatch(handler, parameterJson);
            } catch (InvocationTargetException e) {
                exception.add(new RuntimeException("Failed during dispatch. Method: " + handler
                        + ", Parameter JSON: " + parameterJson, e.getTargetException()));
            }
        }
        if (exception.hasExceptions()) {
            throw exception;
        }
    }

    private void dispatch(String handler, String parameterJson) throws Exception {
        Method method = findMethodUsingName(handler);
        if (method == null) {
            logger.warn("Cannot dispatch: Unable to find method: " + handler + " in " + routeEventsHere.getClass());
            return;
        }
        Object parameter = getParameterFromData(method, parameterJson);
        method.invoke(routeEventsHere, parameter);
    }

    private Method findMethodUsingName(String methodName) {
        Method[] methods = routeEventsHere.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
                return method;
            }
        }
        return null;
    }

    private Object getParameterFromData(Method method, String jsonData) {
        try {
            return new MotechJsonReader().readFromString(jsonData, method.getParameterTypes()[0]);
        } catch (JsonParseException e) {
            return null;
        }
    }
}
