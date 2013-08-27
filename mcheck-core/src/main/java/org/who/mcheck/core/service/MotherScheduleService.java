package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalTime;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.joda.time.LocalDate.parse;
import static org.who.mcheck.core.AllConstants.Schedule.POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME;

@Service
public class MotherScheduleService {
    private final Log log = LogFactory.getLog(MotherScheduleService.class);

    private ScheduleTrackingService scheduleTrackingService;
    private final String preferredCallTime;

    @Autowired
    public MotherScheduleService(ScheduleTrackingService scheduleTrackingService,
                                 @Value("#{mCheck['ivr.preferred.call.time']}") String preferredCallTime) {
        this.scheduleTrackingService = scheduleTrackingService;
        this.preferredCallTime = preferredCallTime;
    }

    public void enroll(String motherId, String registrationDate) {
        EnrollmentRequest scheduleEnrollmentRequest = new EnrollmentRequest()
                .setScheduleName(POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME)
                .setExternalId(motherId)
                .setPreferredAlertTime(new Time(LocalTime.parse(preferredCallTime)))
                .setReferenceDate(parse(registrationDate));

        log.info("Enrolling mother with ID: " + motherId + " to schedule: " + POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME + " preferred call time: " + preferredCallTime);
        scheduleTrackingService.enroll(scheduleEnrollmentRequest);
    }
}
