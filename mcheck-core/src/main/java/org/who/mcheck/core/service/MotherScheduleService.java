package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.AllConstants;
import org.who.mcheck.core.util.DateUtil;
import org.who.mcheck.core.util.IntegerUtil;

import java.text.MessageFormat;

@Service
public class MotherScheduleService {
    private final Log log = LogFactory.getLog(MotherScheduleService.class);
    private final int firstCallDelay;
    private ScheduleTrackingService scheduleTrackingService;
    private String preferredCallTimeInMorning;
    private String preferredCallTimeInAfternoon;

    @Autowired
    public MotherScheduleService(ScheduleTrackingService scheduleTrackingService,
                                 @Value("#{mCheck['ivr.preferred.call.time.morning']}") String preferredCallTimeInMorning,
                                 @Value("#{mCheck['ivr.preferred.call.time.afternoon']}") String preferredCallTimeInAfternoon,
                                 @Value("#{mCheck['first.call.delay']}") String firstCallDelay) {
        this.scheduleTrackingService = scheduleTrackingService;
        this.preferredCallTimeInMorning = preferredCallTimeInMorning;
        this.preferredCallTimeInAfternoon = preferredCallTimeInAfternoon;
        this.firstCallDelay = IntegerUtil.tryParse(firstCallDelay, AllConstants.DEFAULT_FIRST_CALL_DELAY_IN_MINUTES);
    }

    public void enroll(String motherId, LocalDate registrationDate, LocalDate deliveryDate, String dailyCallPreference) {
        int firstSchedule = enrollMotherToFirstDaySchedule(motherId, registrationDate, deliveryDate);
        enrollMotherToSecondDaySchedule(motherId, registrationDate, dailyCallPreference, firstSchedule);
    }

    private int enrollMotherToFirstDaySchedule(String motherId, LocalDate registrationDate, LocalDate deliveryDate) {
        Period periodBetweenDeliveryAndRegistration = new Period(deliveryDate, registrationDate);
        int firstSchedule = periodBetweenDeliveryAndRegistration.getDays() < 2
                ? 1
                : periodBetweenDeliveryAndRegistration.getDays() + 1;
        String firstScheduleName = MessageFormat.format(AllConstants.Schedule.POST_DELIVERY_DANGER_SIGNS_SCHEDULE_TEMPLATE, firstSchedule);
        LocalTime firstCallTime = DateUtil.now().plusMinutes(firstCallDelay);
        enrollToSchedule(motherId, registrationDate, firstScheduleName, firstCallTime);
        return firstSchedule;
    }

    private void enrollMotherToSecondDaySchedule(String motherId, LocalDate registrationDate, String dailyCallPreference, int firstSchedule) {
        String secondCallTime =
                AllConstants.Schedule.MORNING.equalsIgnoreCase(dailyCallPreference)
                        ? preferredCallTimeInMorning
                        : preferredCallTimeInAfternoon;
        String secondScheduleName = MessageFormat.format(AllConstants.Schedule.POST_DELIVERY_DANGER_SIGNS_SCHEDULE_TEMPLATE, firstSchedule + 1);
        LocalDate secondScheduleReferenceDate = registrationDate.plusDays(1);
        enrollToSchedule(motherId, secondScheduleReferenceDate, secondScheduleName, LocalTime.parse(secondCallTime));
    }

    private void enrollToSchedule(String motherId, LocalDate referenceDate, String scheduleName, LocalTime callTime) {
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest()
                .setScheduleName(scheduleName)
                .setExternalId(motherId)
                .setPreferredAlertTime(new Time(callTime))
                .setReferenceDate(referenceDate);
        log.info(MessageFormat.format("Enrolling mother with ID: {0} to schedule: {1}, reference date: {2}, preferred call time: {3}",
                motherId, scheduleName, referenceDate, callTime));
        scheduleTrackingService.enroll(enrollmentRequest);
    }
}
