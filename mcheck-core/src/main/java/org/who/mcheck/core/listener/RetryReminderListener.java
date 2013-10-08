package org.who.mcheck.core.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.who.mcheck.core.AllConstants;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;
import org.who.mcheck.core.service.RetryReminderService;
import org.who.mcheck.core.util.IntegerUtil;

import java.util.HashMap;

import static java.text.MessageFormat.format;
import static org.who.mcheck.core.AllConstants.BirthRegistrationFormFields.CONTACT_NUMBER;

@Component
public class RetryReminderListener {

    private final Log log = LogFactory.getLog(RetryReminderListener.class);
    private final AllCallStatusTokens allCallStatusTokens;
    private final IVRService callService;
    private final String callbackUrl;
    private RetryReminderService retryReminderService;
    private final int maximumNumberOfRetries;

    @Autowired
    public RetryReminderListener(AllCallStatusTokens allCallStatusTokens,
                                 IVRService callService,
                                 RetryReminderService retryReminderService,
                                 @Value("#{mCheck['ivr.callback.url']}") String callbackUrl,
                                 @Value("#{mCheck['ivr.max.retries']}") String maximumNumberOfRetries) {
        this.allCallStatusTokens = allCallStatusTokens;
        this.callService = callService;
        this.retryReminderService = retryReminderService;
        this.callbackUrl = callbackUrl;
        this.maximumNumberOfRetries = IntegerUtil.tryParse(maximumNumberOfRetries, AllConstants.DEFAULT_VALUE_FOR_MAXIMUM_NUMBER_OF_RETRIES);
    }

    @MotechListener(subjects = AllConstants.RETRY_CALL_EVENT_SUBJECT)
    public void retry(MotechEvent event) throws Exception {
        log.info(format("Got a retry call event: {0}", event));

        CallStatusToken token = allCallStatusTokens.findByContactNumber(getParameter(event, CONTACT_NUMBER));
        log.info(format("Found a call status token: {0}", token));

        if (CallStatus.Successful.equals(token.callStatus())) {
            log.info(format("Not attempting a retry of call as the previous attempt was successful. CallStatusToken: {0}", token));
            return;
        }
        if (token.attemptNumber() > maximumNumberOfRetries) {
            log.info(format("Not attempting a retry of call as maximum number of attempts have already been made. Maximum number of retries: {0}, CallStatusToken: {1}",
                    maximumNumberOfRetries, token));
            return;
        }

        retryReminderService.scheduleRetry(token.contactNumber(), token.daySinceDelivery(), token.attemptNumber() + 1);
        CallRequest callRequest = new CallRequest(
                token.contactNumber(),
                new HashMap<String, String>(),
                format(callbackUrl, token.daySinceDelivery()));
        callService.initiateCall(callRequest);
    }

    private String getParameter(MotechEvent event, String name) {
        return (String) event.getParameters().get(name);
    }
}
