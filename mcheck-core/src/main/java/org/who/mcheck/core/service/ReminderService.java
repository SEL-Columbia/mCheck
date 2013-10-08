package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.motechproject.model.Time;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.motechproject.scheduletracking.api.service.EnrollmentRequest;
import org.motechproject.scheduletracking.api.service.ScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.AllConstants;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllCallStatusTokens;
import org.who.mcheck.core.repository.AllMothers;
import org.who.mcheck.core.util.DateUtil;
import org.who.mcheck.core.util.IntegerUtil;
import org.who.mcheck.core.util.LocalTimeUtil;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static org.who.mcheck.core.AllConstants.BirthRegistrationFormFields.CONTACT_NUMBER;

@Service
public class ReminderService {

    private final AllMothers allMothers;
    private final AllCallStatusTokens allCallStatusTokens;
    private final IVRService callService;
    private final ScheduleTrackingService scheduleTrackingService;
    private final String callbackUrl;
    private final PreferredCallTimeService preferredCallTimeService;
    private final MotechSchedulerService motechSchedulerService;
    private final int retryInterval;

    private final Log log = LogFactory.getLog(ReminderService.class);

    @Autowired
    public ReminderService(AllMothers allMothers,
                           AllCallStatusTokens allCallStatusTokens,
                           IVRService callService,
                           ScheduleTrackingService scheduleTrackingService,
                           MotechSchedulerService motechSchedulerService,
                           @Value("#{mCheck['ivr.callback.url']}") String callbackUrl,
                           @Value("#{mCheck['ivr.retry.interval']}") String retryInterval,
                           PreferredCallTimeService preferredCallTimeService) {
        this.allMothers = allMothers;
        this.allCallStatusTokens = allCallStatusTokens;
        this.callService = callService;
        this.scheduleTrackingService = scheduleTrackingService;
        this.motechSchedulerService = motechSchedulerService;
        this.callbackUrl = callbackUrl;
        this.retryInterval = IntegerUtil.tryParse(retryInterval, AllConstants.DEFAULT_VALUE_FOR_RETRY_INTERVAL);
        this.preferredCallTimeService = preferredCallTimeService;
    }

    public void remindMother(String motherId, String scheduleName, String dayWithReferenceToRegistrationDate) {
        Mother mother = allMothers.get(motherId);
        if (mother == null) {
            log.warn("Got alert for a non-registered mother for ID: " + motherId);
            return;
        }

        setupRetryIfCallIsUnsuccessful(dayWithReferenceToRegistrationDate, mother);
        makeTodaysCall(dayWithReferenceToRegistrationDate, mother);
        scheduleTomorrowsCall(motherId, scheduleName, dayWithReferenceToRegistrationDate, mother);
    }

    private void setupRetryIfCallIsUnsuccessful(String dayWithReferenceToRegistrationDate, Mother mother) {
        CallStatusToken callStatusToken = new CallStatusToken(mother.contactNumber(),
                CallStatus.Unsuccessful)
                .withDaySinceDelivery(dayWithReferenceToRegistrationDate)
                .withCallAttemptNumber(1);
        log.info(MessageFormat.format("Creating a CallStatusToken: {0}", callStatusToken));
        allCallStatusTokens.createOrReplaceByPhoneNumber(callStatusToken);

        Date retryTime = LocalTimeUtil.now().plusMinutes(retryInterval).toDateTimeToday().toDate();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(CONTACT_NUMBER, mother.contactNumber());
        parameters.put(MotechSchedulerService.JOB_ID_KEY, UUID.randomUUID().toString());
        MotechEvent event = new MotechEvent(AllConstants.RETRY_CALL_EVENT_SUBJECT, parameters);
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(event, retryTime);

        log.info(MessageFormat.format("Scheduling a retry call job with the following information: {0}", job));
        motechSchedulerService.safeScheduleRunOnceJob(job);
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
                preferredCallTimeService.getPreferredCallTime(mother.dailyCallPreference()));
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
