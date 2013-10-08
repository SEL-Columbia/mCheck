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
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;
import org.who.mcheck.core.util.LocalTimeUtil;

import java.util.HashMap;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetryCallListenerTest {

    @Mock
    private AllCallStatusTokens allCallStatusTokens;
    @Mock
    private MotechSchedulerService motechSchedulerService;
    @Mock
    private IVRService ivrService;
    private RetryCallListener retryCallListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        retryCallListener = new RetryCallListener(
                allCallStatusTokens,
                motechSchedulerService,
                ivrService,
                "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-{0}&trP=Lw&ln=en",
                "5", "2");
    }

    @Test
    public void shouldRetryCallWhenPreviousCallAttemptWasUnsuccessfulAndMaximumNumberOfRetriesHaveNotBeenAttempted() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Unsuccessful)
                .withCallAttemptNumber(1).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryCallListener.retry(motechEvent("contact number 1"));

        CallStatusToken expectedCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Unsuccessful)
                .withCallAttemptNumber(2).withDaySinceDelivery("Day1");
        verify(allCallStatusTokens).addOrReplaceByPhoneNumber(expectedCallStatusToken);
        verify(motechSchedulerService).safeScheduleRunOnceJob(assertJob(new LocalTime(9, 5), "contact number 1"));
        verify(ivrService).initiateCall(assertCallRequest("contact number 1", "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-Day1&trP=Lw&ln=en"));
    }

    @Test
    public void shouldNotScheduleAnotherRetryJobWhenTheCurrentRetryAttemptIsTheMaximum() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Unsuccessful)
                .withCallAttemptNumber(3).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryCallListener.retry(motechEvent("contact number 1"));

        verify(allCallStatusTokens, times(0)).addOrReplaceByPhoneNumber(any(CallStatusToken.class));
        verifyZeroInteractions(ivrService);
        verifyZeroInteractions(motechSchedulerService);
    }

    @Test
    public void shouldNotMakeCallWhenPreviousAttemptWasSuccessful() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        CallStatusToken initialCallStatusToken = new CallStatusToken("contact number 1", CallStatus.Successful)
                .withCallAttemptNumber(1).withDaySinceDelivery("Day1");
        when(allCallStatusTokens.findByContactNumber("contact number 1"))
                .thenReturn(initialCallStatusToken);

        retryCallListener.retry(motechEvent("contact number 1"));

        verify(allCallStatusTokens, times(0)).addOrReplaceByPhoneNumber(any(CallStatusToken.class));
        verifyZeroInteractions(ivrService);
        verifyZeroInteractions(motechSchedulerService);
    }

    private MotechEvent motechEvent(String contactNumber) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("contactNumber", contactNumber);
        parameters.put(MotechSchedulerService.JOB_ID_KEY, "job id 1");
        return new MotechEvent("RETRY-CALL-EVENT", parameters);
    }

    private RunOnceSchedulableJob assertJob(final LocalTime retryTime, final String contactNumber) {
        return argThat(new ArgumentMatcher<RunOnceSchedulableJob>() {
            @Override
            public boolean matches(Object o) {
                RunOnceSchedulableJob job = (RunOnceSchedulableJob) o;
                return retryTime.toDateTimeToday().toDate().equals(job.getStartDate())
                        && "RETRY-CALL-EVENT".equals(job.getMotechEvent().getSubject())
                        && job.getMotechEvent().getParameters().get("contactNumber").equals(contactNumber)
                        && job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY) != null;
            }
        });
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
