package org.who.formhub.api.util;

import com.google.gson.stream.JsonReader;
import org.who.formhub.api.contract.FormHubHttpResponse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gson.stream.JsonToken.*;

public class FormHubResponseJSONParser {

    public static List<Map<String, String>> parse(FormHubHttpResponse response) {
        List<Map<String, String>> formData = new ArrayList<>();

        try (JsonReader jsonReader = new JsonReader(new StringReader(response.contentAsString()))) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                formData.add(readForm(jsonReader));
            }
            jsonReader.endArray();
        } catch (Exception e) {
            throw new RuntimeException(response.contentAsString() + e);
        }
        return formData;
    }

    private static Map<String, String> readForm(JsonReader jsonReader) throws IOException {
        HashMap<String, String> fieldValues = new HashMap<>();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (jsonReader.peek() == STRING) {
                fieldValues.put(name, jsonReader.nextString());
            } else if (jsonReader.peek() == NUMBER) {
                fieldValues.put(name, Long.toString(jsonReader.nextLong()));
            } else if (jsonReader.peek() == NULL) {
                jsonReader.skipValue();
                fieldValues.put(name, "");
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return fieldValues;
    }
}
