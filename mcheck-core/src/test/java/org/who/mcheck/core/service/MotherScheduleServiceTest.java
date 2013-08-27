package org.who.mcheck.core.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;

import static org.joda.time.LocalDate.parse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotherScheduleServiceTest {
    @Mock
    private ScheduleTrackingService scheduleTrackingService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldEnrollMotherToPostPregnancyDangerSignsSchedule() throws Exception {
        MotherScheduleService service = new MotherScheduleService(scheduleTrackingService, "14:00:00");
        service.enroll("id", "2013-01-01");

        verify(scheduleTrackingService).enroll(enrollmentFor("id", "Post Delivery Danger Signs", parse("2013-01-01")));
    }

    private EnrollmentRequest enrollmentFor(final String externalId, final String scheduleName, final LocalDate referenceDate) {
        return argThat(new ArgumentMatcher<EnrollmentRequest>() {
            @Override
            public boolean matches(Object o) {
                EnrollmentRequest request = (EnrollmentRequest) o;
                return externalId.equals(request.getExternalId()) && referenceDate.equals(request.getReferenceDate())
                        && scheduleName.equals(request.getScheduleName());
            }
        });
    }
}
