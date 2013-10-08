package org.who.mcheck.core.service;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;
import org.who.mcheck.core.util.LocalTimeUtil;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetryReminderServiceTest {

    @Mock
    private AllCallStatusTokens allCallStatusTokens;
    @Mock
    private MotechSchedulerService motechSchedulerService;

    private RetryReminderService retryReminderService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        retryReminderService = new RetryReminderService(allCallStatusTokens, motechSchedulerService, "5");
    }

    @Test
    public void shouldSetupRetryWhenCallIsMade() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));

        retryReminderService.scheduleRetry("1234567890", "Day4", 2);

        verify(allCallStatusTokens).addOrReplaceByPhoneNumber(
                new CallStatusToken("1234567890", CallStatus.Unsuccessful)
                        .withDaySinceDelivery("Day4")
                        .withCallAttemptNumber(2));
        verify(motechSchedulerService).safeScheduleRunOnceJob(assertJob(new LocalTime(9, 5), "1234567890"));
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
}
