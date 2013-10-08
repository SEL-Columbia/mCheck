package org.who.mcheck.core.listener;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.scheduler.MotechSchedulerService;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;
import org.who.mcheck.core.service.RetryReminderService;
import org.who.mcheck.core.util.LocalTimeUtil;

import java.util.HashMap;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetryReminderListenerTest {

    @Mock
    private AllCallStatusTokens allCallStatusTokens;
    @Mock
    private RetryReminderService retryReminderService;
    @Mock
    private IVRService ivrService;
    private RetryReminderListener retryReminderListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        retryReminderListener = new RetryReminderListener(
                allCallStatusTokens,
                ivrService,
                retryReminderService, "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-{0}&trP=Lw&ln=en",
                "2"
        );
    }

    @Test
    public void shouldRetryCallWhenPreviousCallAttemptWasUnsuccessfulAndMaximumNumberOfRetriesHaveNotBeenAttempted() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Unsuccessful)
                .withCallAttemptNumber(1).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryReminderListener.retry(motechEvent("contact number 1"));

        verify(retryReminderService).scheduleRetry("contact number 1", "Day1", 2);
        verify(ivrService).initiateCall(assertCallRequest("contact number 1", "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-Day1&trP=Lw&ln=en"));
    }

    @Test
    public void shouldNotScheduleAnotherRetryJobWhenTheCurrentRetryAttemptIsTheMaximum() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Unsuccessful)
                .withCallAttemptNumber(3).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryReminderListener.retry(motechEvent("contact number 1"));

        verifyZeroInteractions(retryReminderService);
        verifyZeroInteractions(ivrService);

    }

    @Test
    public void shouldNotMakeCallWhenPreviousAttemptWasSuccessful() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Successful)
                .withCallAttemptNumber(1).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryReminderListener.retry(motechEvent("contact number 1"));

        verifyZeroInteractions(retryReminderService);
        verifyZeroInteractions(ivrService);
    }

    private MotechEvent motechEvent(String contactNumber) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("contactNumber", contactNumber);
        parameters.put(MotechSchedulerService.JOB_ID_KEY, "job id 1");
        return new MotechEvent("RETRY-CALL-EVENT", parameters);
    }

    private CallRequest assertCallRequest(final String phoneNumber, final String callbackUrl) {
        return argThat(new ArgumentMatcher<CallRequest>() {
            @Override
            public boolean matches(Object o) {
                CallRequest callRequest = (CallRequest) o;
                return phoneNumber.equals(callRequest.getPhone()) && callbackUrl.equals(callRequest.getCallBackUrl());
            }
        });
    }
}
