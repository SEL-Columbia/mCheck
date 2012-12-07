package org.who.formhub.api.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.who.formhub.api.contract.FormHubHttpResponse;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;

@Component
public class FormHubHttpClient {
    private DefaultHttpClient httpClient;
    private final ReentrantLock lock = new ReentrantLock();
    private static Logger logger = LoggerFactory.getLogger(FormHubHttpClient.class);

    public FormHubHttpClient() {
        this.httpClient = new DefaultHttpClient();
    }

    public FormHubHttpResponse get(String url, String baseURL, String userName, String password) {
        logger.debug("Fetching URL: " + url + " with username: " + userName + " with formHubBaseUrl: " + baseURL);

        FormHubHttpResponse formHubHttpResponse = null;

        lock.lock();
        try {
            httpClient.getParams().setParameter(CONNECTION_TIMEOUT, 60000);
            httpClient.getParams().setParameter(SO_TIMEOUT, 60000);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
            HttpGet httpGet = new HttpGet(url);
            Header header = new BasicScheme().authenticate(credentials, httpGet);
            httpGet.addHeader(header);
            HttpResponse response = httpClient.execute(httpGet);
            Header[] headers = response.getAllHeaders();

            formHubHttpResponse = failureResponse(response.getStatusLine().getStatusCode(), Arrays.toString(headers).getBytes());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                formHubHttpResponse = new FormHubHttpResponse(response.getStatusLine().getStatusCode(), IOUtils.toByteArray(entity.getContent()));
            }
        } catch (Exception e) {
            return failureResponse(404, e.getMessage().getBytes());
        } finally {
            lock.unlock();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Got response for URL: " + url + ": " + formHubHttpResponse);
        }

        return formHubHttpResponse;
    }

    private FormHubHttpResponse failureResponse(int statusCode, byte[] responseContent) {
        return new FormHubHttpResponse(statusCode, responseContent);
    }
}
