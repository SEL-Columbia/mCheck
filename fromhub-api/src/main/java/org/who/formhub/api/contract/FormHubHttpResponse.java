package org.who.formhub.api.contract;

public class FormHubHttpResponse {
    private final int statusCode;
    private final byte[] responseContent;

    public FormHubHttpResponse(int statusCode, byte[] responseContent) {
        this.statusCode = statusCode;
        this.responseContent = responseContent;
    }

    public String contentAsString() {
        return new String(responseContent);
    }

    public boolean isValid() {
        return statusCode == 200;
    }

    @Override
    public String toString() {
        return "Status code: " + statusCode + ", Content: " + contentAsString();
    }
}
