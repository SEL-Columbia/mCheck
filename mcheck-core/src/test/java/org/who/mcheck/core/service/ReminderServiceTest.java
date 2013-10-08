package org.who.mcheck.core.service;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllReminderStatusTokens;
import org.who.mcheck.core.repository.AllMothers;
import org.who.mcheck.core.util.DateUtil;
import org.who.mcheck.core.util.LocalTimeUtil;

import static org.joda.time.LocalDate.parse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReminderServiceTest {
    @Mock
    private AllMothers allMothers;
    @Mock
    private AllReminderStatusTokens allReminderStatusTokens;
    @Mock
    private IVRService ivrService;
    @Mock
    private ScheduleTrackingService scheduleTrackingService;
    @Mock
    private RetryReminderService retryReminderService;
    @Mock
    private PreferredReminderTimeService preferredReminderTimeService;
    private ReminderService reminderService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reminderService = new ReminderService(
                allMothers,
                allReminderStatusTokens, ivrService,
                scheduleTrackingService,
                preferredReminderTimeService, retryReminderService, "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-{0}&trP=Lw&ln=en"
        );
    }

    @Test
    public void shouldInitiateACallToRemindMother() throws Exception {
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.get("mother id")).thenReturn(mother);
        when(preferredReminderTimeService.getPreferredCallTime("morning")).thenReturn(LocalTime.parse("09:30:00"));

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 4", "Day4");

        verify(ivrService).initiateCall(assertCallRequest(mother.contactNumber(), "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-Day4&trP=Lw&ln=en"));
    }

    @Test
    public void shouldFulfillCurrentMilestoneWhenMotherIsReminded() throws Exception {
        DateUtil.fakeIt(LocalDate.parse("2012-01-01"));
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.get("mother id")).thenReturn(mother);
        when(preferredReminderTimeService.getPreferredCallTime("morning")).thenReturn(LocalTime.parse("09:30:00"));

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 4", "Day4");

        verify(scheduleTrackingService).fulfillCurrentMilestone("mother id", "Post Delivery Danger Signs - Day 4", LocalDate.parse("2012-01-01"));
    }

    @Test
    public void shouldEnrollMotherToNextDayScheduleWhenMotherIsReminded() throws Exception {
        DateUtil.fakeIt(LocalDate.parse("2012-01-01"));
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.get("mother id")).thenReturn(mother);
        when(preferredReminderTimeService.getPreferredCallTime("morning")).thenReturn(LocalTime.parse("09:30:00"));

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 4", "Day4");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("mother id", "Post Delivery Danger Signs - Day 5", parse("2012-01-02"), new Time(9, 30)));

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 5", "Day5");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("mother id", "Post Delivery Danger Signs - Day 6", parse("2012-01-02"), new Time(9, 30)));
    }

    @Test
    public void shouldNotEnrollMotherToNextDayScheduleWhenTheCurrentReminderIsTheLast() throws Exception {
        DateUtil.fakeIt(LocalDate.parse("2012-01-01"));
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.get("mother id")).thenReturn(mother);

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 7", "Day7");

        verify(scheduleTrackingService).fulfillCurrentMilestone("mother id", "Post Delivery Danger Signs - Day 7", LocalDate.parse("2012-01-01"));
        verifyNoMoreInteractions(scheduleTrackingService);
    }

    @Test
    public void shouldNotInitiateACallToRemindMotherWhenMotherNotFound() throws Exception {
        when(allMothers.get("mother id")).thenReturn(null);

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 1", "Day1");

        verifyZeroInteractions(ivrService);
        verifyZeroInteractions(scheduleTrackingService);
    }

    @Test
    public void shouldSetupRetryWhenCallIsMade() throws Exception {
        LocalTimeUtil.fakeIt(new LocalTime(9, 0));
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.get("mother id")).thenReturn(mother);
        when(preferredReminderTimeService.getPreferredCallTime("morning")).thenReturn(LocalTime.parse("09:30:00"));

        reminderService.remindMother("mother id", "Post Delivery Danger Signs - Day 4", "Day4");

        verify(retryReminderService).scheduleRetry("1234567890", "Day4", 1);
    }

    private EnrollmentRequest enrollmentFor(final String externalId, final String scheduleName,
                                            final LocalDate referenceDate, final Time preferredAlertTime) {
        return argThat(new ArgumentMatcher<EnrollmentRequest>() {
            @Override
            public boolean matches(Object o) {
                EnrollmentRequest request = (EnrollmentRequest) o;
                return externalId.equals(request.getExternalId()) && referenceDate.equals(request.getReferenceDate())
                        && scheduleName.equals(request.getScheduleName())
                        && preferredAlertTime.equals(request.getPreferredAlertTime()
                );
            }
        });
    }

    private CallRequest assertCallRequest(final String phoneNumber, final String callbackUrl) {
        return argThat(new ArgumentMatcher<CallRequest>() {
            @Override
            public boolean matches(Object o) {
                CallRequest callRequest = (CallRequest) o;
                return phoneNumber.equals(callRequest.getPhone())
                        && callbackUrl.equals(callRequest.getCallBackUrl());
            }
        });
    }

}
