package org.who.mcheck.integration;

import org.junit.Before;
import org.junit.Test;
import org.who.formhub.api.contract.FormHubFormDefinition;
import org.who.formhub.api.util.FormHubImportProperties;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.controller.MCheckController;

import java.text.MessageFormat;
import java.util.*;

import static org.junit.Assert.fail;

public class FormHubImportFormDefinitionsJSONTest {

    private final String definitionsJSONPath = "formhub-export.json";
    private List<FormHubFormDefinition> forms = new ArrayList<>();

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.put(FormHubImportProperties.FORMHUB_IMPORT_DEFINITION_FILE, "/" + definitionsJSONPath);
        FormHubImportProperties importProperties = new FormHubImportProperties(properties);

        forms.addAll(importProperties.importDefinition().forms());
    }

    @Test
    public void everyFormInTheJSONShouldHaveAllTheCorrectMappings() {
        Map<String, Class<?>> classEveryFormMappingConvertsTo = new HashMap<>();

        classEveryFormMappingConvertsTo.put("mcheck", MotherRegistrationRequest.class);

        assertEveryFormDefinitionInTheJSONHasBeenRepresentedInThisTest(classEveryFormMappingConvertsTo);
        assertThatTheControllerHasTheMethodsCorrespondingToTheseFormNames(MCheckController.class, classEveryFormMappingConvertsTo);
    }

    private void assertEveryFormDefinitionInTheJSONHasBeenRepresentedInThisTest(Map<String, Class<?>> classEveryFormMappingConvertsTo) {
        for (FormHubFormDefinition formDefinition : forms) {
            String formName = formDefinition.name();
            Class<?> typeUsedForMappingsInForm = classEveryFormMappingConvertsTo.get(formName);

            if (typeUsedForMappingsInForm == null) {
                fail("Missing form. Change this test to add a mapping for the form: " + formName + " above.");
            }

            assertThatRightHandSideOfEveryFormMappingMapsToAFieldInTheObject(formName, typeUsedForMappingsInForm);
        }
    }

    private void assertThatRightHandSideOfEveryFormMappingMapsToAFieldInTheObject(String formName, Class<?> typeUsedForObjectsInForm) {
        ArrayList<FormHubFormDefinition> formDefinitions = findForms(formName);
        for (FormHubFormDefinition formDefinition : formDefinitions) {
            for (String fieldInObject : formDefinition.mappings().values()) {
                try {
                    typeUsedForObjectsInForm.getDeclaredField(fieldInObject);
                } catch (NoSuchFieldException e) {
                    fail("Could not find field: " + fieldInObject + " in class: " + typeUsedForObjectsInForm +
                            ". Check the form: " + formName + " in " + definitionsJSONPath + ".");
                }
            }
        }
    }

    private void assertThatTheControllerHasTheMethodsCorrespondingToTheseFormNames(Class<?> controllerClass, Map<String, Class<?>> classEveryFormMappingConvertsTo) {
        for (Map.Entry<String, Class<?>> formNameToClassEntry : classEveryFormMappingConvertsTo.entrySet()) {
            String formName = formNameToClassEntry.getKey();
            Class<?> parameterTypeOfTheMethod = formNameToClassEntry.getValue();
            ArrayList<FormHubFormDefinition> definition = findForms(formName);
            for (FormHubFormDefinition formDefinition : definition) {
                ensureMethodPresent(controllerClass, formDefinition.handler(), parameterTypeOfTheMethod);
            }
        }
    }

    private void ensureMethodPresent(Class<?> controllerClass, String formName, Class<?> parameterTypeOfTheMethod) {
        try {
            controllerClass.getDeclaredMethod(formName, parameterTypeOfTheMethod);
        } catch (NoSuchMethodException e) {
            fail(MessageFormat.format("There should be a method in {0} like this: public void {1}({2} request). If it is " +
                    "not present, the dispatcher will not be able to do anything for form submissions of this form: {3}.",
                    controllerClass.getSimpleName(), formName, parameterTypeOfTheMethod.getSimpleName(), formName));
        }
    }

    private ArrayList<FormHubFormDefinition> findForms(String formName) {
        ArrayList<FormHubFormDefinition> definitions = new ArrayList<>();
        for (FormHubFormDefinition definition : forms) {
            if (definition.name().equals(formName)) {
                definitions.add(definition);
            }
        }
        return definitions;
    }

}