package org.who.mcheck.core.service;

import org.joda.time.LocalTime;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.joda.time.LocalDate.parse;

@Service
public class MotherScheduleService {
    public static final String POST_PREGNANCY_DANGER_SIGNS_SCHEDULE_NAME = "Post Pregnancy Danger Signs";
    public static final Time PREFERRED_ALERT_TIME = new Time(new LocalTime(14, 0));
    private ScheduleTrackingService scheduleTrackingService;

    @Autowired
    public MotherScheduleService(ScheduleTrackingService scheduleTrackingService) {
        this.scheduleTrackingService = scheduleTrackingService;
    }

    public void enroll(String motherId, String registrationDate) {
        EnrollmentRequest scheduleEnrollmentRequest = new EnrollmentRequest()
                .setScheduleName(POST_PREGNANCY_DANGER_SIGNS_SCHEDULE_NAME)
                .setExternalId(motherId)
                .setPreferredAlertTime(PREFERRED_ALERT_TIME)
                .setReferenceDate(parse(registrationDate));

        scheduleTrackingService.enroll(scheduleEnrollmentRequest);
    }
}
