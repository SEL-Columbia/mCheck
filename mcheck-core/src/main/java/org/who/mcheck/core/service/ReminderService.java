package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.AllConstants;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllReminderStatusTokens;
import org.who.mcheck.core.repository.AllMothers;
import org.who.mcheck.core.util.DateUtil;
import org.who.mcheck.core.util.IntegerUtil;

import java.text.MessageFormat;
import java.util.HashMap;

@Service
public class ReminderService {

    private final Log log = LogFactory.getLog(ReminderService.class);

    private final AllMothers allMothers;
    private final AllReminderStatusTokens allReminderStatusTokens;
    private final IVRService callService;
    private final ScheduleTrackingService scheduleTrackingService;
    private final String callbackUrl;
    private final PreferredReminderTimeService preferredReminderTimeService;
    private RetryReminderService retryReminderService;

    @Autowired
    public ReminderService(AllMothers allMothers,
                           AllReminderStatusTokens allReminderStatusTokens,
                           IVRService callService,
                           ScheduleTrackingService scheduleTrackingService,
                           PreferredReminderTimeService preferredReminderTimeService,
                           RetryReminderService retryReminderService,
                           @Value("#{mCheck['ivr.callback.url']}") String callbackUrl) {
        this.allMothers = allMothers;
        this.allReminderStatusTokens = allReminderStatusTokens;
        this.callService = callService;
        this.scheduleTrackingService = scheduleTrackingService;
        this.preferredReminderTimeService = preferredReminderTimeService;
        this.retryReminderService = retryReminderService;
        this.callbackUrl = callbackUrl;
    }

    public void remindMother(String motherId, String scheduleName, String dayWithReferenceToRegistrationDate) {
        Mother mother = allMothers.get(motherId);
        if (mother == null) {
            log.warn("Got alert for a non-registered mother for ID: " + motherId);
            return;
        }

        retryReminderService.scheduleRetry(mother.contactNumber(), dayWithReferenceToRegistrationDate, 1);
        makeTodaysCall(dayWithReferenceToRegistrationDate, mother);
        scheduleTomorrowsCall(motherId, scheduleName, dayWithReferenceToRegistrationDate, mother);
    }

    private void makeTodaysCall(String dayWithReferenceToRegistrationDate, Mother mother) {
        log.info(MessageFormat.format("Calling mother: {0}. Call back URL: {1}", mother, MessageFormat.format(callbackUrl, dayWithReferenceToRegistrationDate)));
        CallRequest callRequest = new CallRequest(
                mother.contactNumber(),
                new HashMap<String, String>(),
                MessageFormat.format(callbackUrl, dayWithReferenceToRegistrationDate));
        callService.initiateCall(callRequest);
    }

    private void scheduleTomorrowsCall(String motherId, String scheduleName, String dayWithReferenceToRegistrationDate, Mother mother) {
        LocalDate today = DateUtil.today();
        scheduleTrackingService.fulfillCurrentMilestone(motherId, scheduleName, today);

        int nextCall = IntegerUtil.tryParse(
                String.valueOf(dayWithReferenceToRegistrationDate.charAt(dayWithReferenceToRegistrationDate.length() - 1)),
                0) + 1;

        if (nextCall > AllConstants.Schedule.LAST_DAY_OF_POST_DELIVERY_DANGER_SIGNS_SCHEDULE) {
            log.info("Not enrolling mother to subsequent calls as the current call is the last call.");
            return;
        }
        enrollToSchedule(motherId,
                today.plusDays(1),
                MessageFormat.format(AllConstants.Schedule.POST_DELIVERY_DANGER_SIGNS_SCHEDULE_TEMPLATE, nextCall),
                preferredReminderTimeService.getPreferredCallTime(mother.dailyCallPreference()));
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
