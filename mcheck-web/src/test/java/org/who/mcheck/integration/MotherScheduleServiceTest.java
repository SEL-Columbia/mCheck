package org.who.mcheck.integration;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.domain.Schedule;
import org.motechproject.scheduletracking.api.domain.ScheduleFactory;
import org.motechproject.scheduletracking.api.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.motechproject.scheduletracking.api.repository.TrackedSchedulesJsonReaderImpl;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.who.mcheck.core.service.MotherScheduleService;
import org.who.mcheck.core.util.DateUtil;

import java.util.List;

import static org.joda.time.LocalDate.parse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-mcheck-web.xml")
public class MotherScheduleServiceTest {
    @Mock
    private ScheduleTrackingService scheduleTrackingService;
    @Mock
    private AllSchedules allSchedules;
    @Value("#{schedule_tracking['schedule.definitions.directory']}")
    private String schedulesDirectory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldEnrollMotherToPostPregnancyDangerSignsScheduleBasedOnRegistrationDate() throws Exception {
        MotherScheduleService service = new MotherScheduleService(scheduleTrackingService, allSchedules, "09:30:00", "14:30:00");
        TrackedSchedulesJsonReaderImpl schedulesReader = new TrackedSchedulesJsonReaderImpl();
        List<ScheduleRecord> allScheduleRecords = schedulesReader.getAllSchedules(schedulesDirectory);
        ScheduleRecord scheduleRecord = null;
        for (ScheduleRecord record : allScheduleRecords) {
            if ("Post Delivery Danger Signs".equalsIgnoreCase(record.name())) {
                scheduleRecord = record;
            }
        }

        Schedule schedule = new ScheduleFactory().build(scheduleRecord);

        when(allSchedules.getByName("Post Delivery Danger Signs")).thenReturn(schedule);

        DateUtil.fakeIt(parse("2013-08-30"));
        service.enroll("id", "2013-08-30", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-30"), "Day1", new Time(LocalTime.parse("09:30:00"))));
        service.enroll("id", "2013-08-29", "afternoon");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-29"), "Day2", new Time(LocalTime.parse("14:30:00"))));
        service.enroll("id", "2013-08-28", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-28"), "Day3", new Time(LocalTime.parse("09:30:00"))));
        service.enroll("id", "2013-08-27", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-27"), "Day4", new Time(LocalTime.parse("09:30:00"))));
        service.enroll("id", "2013-08-26", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-26"), "Day5", new Time(LocalTime.parse("09:30:00"))));
        service.enroll("id", "2013-08-25", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-25"), "Day6", new Time(LocalTime.parse("09:30:00"))));
        service.enroll("id", "2013-08-24", "morning");
        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-08-24"), "Day7", new Time(LocalTime.parse("09:30:00"))));
    }

    private EnrollmentRequest enrollmentFor(final String externalId, final String scheduleName,
                                            final LocalDate referenceDate, final String startingMilestone, final Time preferredAlertTime) {
        return argThat(new ArgumentMatcher<EnrollmentRequest>() {
            @Override
            public boolean matches(Object o) {
                EnrollmentRequest request = (EnrollmentRequest) o;
                return externalId.equals(request.getExternalId()) && referenceDate.equals(request.getReferenceDate())
                        && scheduleName.equals(request.getScheduleName())
                        && startingMilestone.equals(request.getStartingMilestoneName())
                        && preferredAlertTime.equals(request.getPreferredAlertTime()
                );
            }
        });
    }
}
