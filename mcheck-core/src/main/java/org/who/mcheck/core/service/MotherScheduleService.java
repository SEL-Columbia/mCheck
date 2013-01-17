package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.joda.time.LocalDate.parse;
import static org.who.mcheck.core.AllConstants.Schedule.POST_PREGNANCY_DANGER_SIGNS_SCHEDULE_NAME;
import static org.who.mcheck.core.AllConstants.Schedule.PREFERRED_ALERT_TIME;

@Service
public class MotherScheduleService {
    private final Log log = LogFactory.getLog(MotherScheduleService.class);

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

        log.info("Enrolling mother to 'Post Pregnancy Danger Signs' schedule. ID: " + motherId);
        scheduleTrackingService.enroll(scheduleEnrollmentRequest);
    }
}
