package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.AllConstants;
import org.who.mcheck.core.domain.ReminderStatus;
import org.who.mcheck.core.domain.ReminderStatusToken;
import org.who.mcheck.core.repository.AllReminderStatusTokens;
import org.who.mcheck.core.util.IntegerUtil;
import org.who.mcheck.core.util.LocalTimeUtil;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class RetryReminderService {
    private final Log log = LogFactory.getLog(RetryReminderService.class);

    private final MotechSchedulerService motechSchedulerService;
    private final boolean shouldRetry;
    private AllReminderStatusTokens allReminderStatusTokens;
    private final int retryInterval;

    @Autowired
    public RetryReminderService(AllReminderStatusTokens allReminderStatusTokens,
                                MotechSchedulerService motechSchedulerService,
                                @Value("#{mCheck['ivr.retry.interval']}") String retryInterval,
                                @Value("#{mCheck['ivr.should.retry']}") String shouldRetry) {
        this.allReminderStatusTokens = allReminderStatusTokens;
        this.motechSchedulerService = motechSchedulerService;
        this.retryInterval = IntegerUtil.tryParse(retryInterval, AllConstants.DEFAULT_VALUE_FOR_RETRY_INTERVAL);
        this.shouldRetry = Boolean.parseBoolean(shouldRetry);
    }

    public void scheduleRetry(String contactNumber, String day, int attemptNumber) {
        if (!shouldRetry) {
            log.info("Retry disabled, so not scheduling retry jobs");
            return;
        }
        ReminderStatusToken reminderStatusToken = new ReminderStatusToken(contactNumber,
                ReminderStatus.Unsuccessful)
                .withDaySinceDelivery(day)
                .withCallAttemptNumber(attemptNumber);
        log.info(MessageFormat.format("Creating or updating ReminderStatusToken for next retry attempt: {0}", reminderStatusToken));
        allReminderStatusTokens.addOrReplaceByPhoneNumber(reminderStatusToken);

        Date retryTime = LocalTimeUtil.now().plusMinutes(retryInterval).toDateTimeToday().toDate();
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(AllConstants.BirthRegistrationFormFields.CONTACT_NUMBER, contactNumber);
        parameters.put(MotechSchedulerService.JOB_ID_KEY, UUID.randomUUID().toString());
        MotechEvent event = new MotechEvent(AllConstants.RETRY_CALL_EVENT_SUBJECT, parameters);
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(event, retryTime);

        log.info(MessageFormat.format("Scheduling a retry call job with the following information: {0}", job));
        motechSchedulerService.safeScheduleRunOnceJob(job);
    }
}