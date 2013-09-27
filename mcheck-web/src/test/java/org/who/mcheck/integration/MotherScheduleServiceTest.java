package org.who.mcheck.integration;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.who.mcheck.core.service.MotherScheduleService;
import org.who.mcheck.core.util.DateUtil;

import static org.joda.time.LocalDate.parse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotherScheduleServiceTest {
    @Mock
    private ScheduleTrackingService scheduleTrackingService;

    private MotherScheduleService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new MotherScheduleService(scheduleTrackingService, "09:30:00", "14:30:00", "5");
    }

    @Test
    public void shouldEnrollMotherWithFirstScheduleAsDay1ScheduleWhenRegistrationDateAndDeliveryDateAreSame() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-01"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 1", parse("2013-01-01"), new Time(9, 5)));
    }

    @Test
    public void shouldEnrollMotherWithFirstScheduleAsDay1ScheduleWhenRegistrationDateIsOneDayAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-02"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 1", parse("2013-01-02"), new Time(9, 5)));
    }

    @Test
    public void shouldEnrollMotherWithFirstScheduleAsDay3ScheduleWhenRegistrationDateIsTwoDaysAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-03"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 3", parse("2013-01-03"), new Time(9, 5)));
    }

    @Test
    public void shouldEnrollMotherWithFirstScheduleAsDay4ScheduleWhenRegistrationDateIsThreeDaysAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-04"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 4", parse("2013-01-04"), new Time(9, 5)));
    }

    @Test
    public void shouldEnrollMotherToDay2ScheduleOnTheNextDayOfRegistrationWhenRegistrationDateAndDeliveryDateAreSame() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-01"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 2", parse("2013-01-02"), new Time(9, 30)));
    }

    @Test
    public void shouldEnrollMotherToDay2ScheduleOnTheNextDayOfRegistrationWhenRegistrationDateIsOneDayAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-02"), parse("2013-01-01"), "afternoon");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 2", parse("2013-01-03"), new Time(14, 30)));
    }

    @Test
    public void shouldEnrollMotherToDay4ScheduleOnTheNextDayOfRegistrationWhenRegistrationDateIsTwoDaysAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-03"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 4", parse("2013-01-04"), new Time(9, 30)));
    }

    @Test
    public void shouldEnrollMotherToDay5ScheduleOnTheNextDayOfRegistrationWhenRegistrationDateIsThreeDaysAfterDeliveryDate() throws Exception {
        DateUtil.fakeTime(new LocalTime(9, 0));

        service.enroll("id", parse("2013-01-04"), parse("2013-01-01"), "morning");

        verify(scheduleTrackingService)
                .enroll(enrollmentFor("id", "Post Delivery Danger Signs - Day 5", parse("2013-01-05"), new Time(9, 30)));
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
}
