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
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;
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
    private AllCallStatusTokens allCallStatusTokens;
    private final int retryInterval;

    @Autowired
    public RetryReminderService(AllCallStatusTokens allCallStatusTokens,
                                MotechSchedulerService motechSchedulerService,
                                @Value("#{mCheck['ivr.retry.interval']}") String retryInterval) {
        this.allCallStatusTokens = allCallStatusTokens;
        this.motechSchedulerService = motechSchedulerService;
        this.retryInterval = IntegerUtil.tryParse(retryInterval, AllConstants.DEFAULT_VALUE_FOR_RETRY_INTERVAL);
    }

    public void scheduleRetry(String contactNumber, String day, int attemptNumber) {
        CallStatusToken callStatusToken = new CallStatusToken(contactNumber,
                CallStatus.Unsuccessful)
                .withDaySinceDelivery(day)
                .withCallAttemptNumber(attemptNumber);
        log.info(MessageFormat.format("Creating or updating CallStatusToken for next retry attempt: {0}", callStatusToken));
        allCallStatusTokens.addOrReplaceByPhoneNumber(callStatusToken);

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