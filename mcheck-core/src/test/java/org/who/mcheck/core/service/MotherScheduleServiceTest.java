package org.who.mcheck.core.service;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.scheduletracking.api.domain.Milestone;
import org.motechproject.scheduletracking.api.domain.Schedule;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.who.mcheck.core.util.DateUtil;

import static org.joda.time.LocalDate.parse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotherScheduleServiceTest {
    @Mock
    private ScheduleTrackingService scheduleTrackingService;
    @Mock
    private AllSchedules allSchedules;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldEnrollMotherToPostPregnancyDangerSignsSchedule() throws Exception {
        MotherScheduleService service = new MotherScheduleService(scheduleTrackingService, allSchedules, "14:00:00");
        Milestone firstMilestone = new Milestone("Day1", days(0), days(0), days(0), days(1));
        Milestone secondMilestone = new Milestone("Day2", days(2), days(2), days(2), days(2));
        Schedule schedule = new Schedule("Post Delivery Danger Signs");
        schedule.addMilestones(firstMilestone);
        schedule.addMilestones(secondMilestone);

        when(allSchedules.getByName("Post Delivery Danger Signs")).thenReturn(schedule);

        DateUtil.fakeIt(parse("2013-01-01"));
        service.enroll("id", "2013-01-01");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-01-01"), "Day1"));

        DateUtil.fakeIt(parse("2013-01-02"));
        service.enroll("id", "2013-01-01");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-01-01"), "Day2"));
    }

    private EnrollmentRequest enrollmentFor(final String externalId, final String scheduleName,
                                            final LocalDate referenceDate, final String startingMilestone) {
        return argThat(new ArgumentMatcher<EnrollmentRequest>() {
            @Override
            public boolean matches(Object o) {
                EnrollmentRequest request = (EnrollmentRequest) o;
                return externalId.equals(request.getExternalId()) && referenceDate.equals(request.getReferenceDate())
                        && scheduleName.equals(request.getScheduleName())
                        && startingMilestone.equals(request.getStartingMilestoneName());
            }
        });
    }

    private Period days(int numberOfDays) {
        return new Period(0, 0, 0, numberOfDays, 0, 0, 0, 0);
    }
}
