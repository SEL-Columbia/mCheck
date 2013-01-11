package org.who.mcheck.scheduler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.who.formhub.listener.FormHubListener;

import java.util.Date;
import java.util.HashMap;

import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;

@Component
public class MCheckScheduler {
    private static final String SUBJECT = "MCHECK-TIMED-SCHEDULE";

    private final MotechSchedulerService service;
    private final FormHubListener formHubListener;
    private final Long mCheckPollInterval;
    private final int mCheckPollStartTime;
    private static Logger logger = LoggerFactory.getLogger(MCheckScheduler.class);
    private boolean shouldFetchForms;

    @Autowired
    public MCheckScheduler(MotechSchedulerService service, FormHubListener formHubListener,
                           @Value("#{mCheck['mCheck.poll.start.time']}") int mCheckPollStartTime,
                           @Value("#{mCheck['mCheck.poll.time.interval']}") Long mCheckPollInterval) {
        this.service = service;
        this.formHubListener = formHubListener;
        this.mCheckPollInterval = mCheckPollInterval;
        this.mCheckPollStartTime = mCheckPollStartTime;
    }

    public void startTimedScheduler() {
        logger.info("Scheduling FormHub fetch ...");

        Date startTime = now().plusSeconds(mCheckPollStartTime).toDate();
        MotechEvent event = new MotechEvent(SUBJECT, new HashMap<String, Object>());
        RepeatingSchedulableJob job = new RepeatingSchedulableJob(event, startTime, null, mCheckPollInterval * MILLIS_PER_SECOND, true);

        service.safeScheduleRepeatingJob(job);

        startListening();
    }

    private void startListening() {
        this.shouldFetchForms = true;
    }

    @MotechListener(subjects = SUBJECT)
    public void fetchMCheckForms(MotechEvent event) throws Exception {
        if (!shouldFetchForms) return;

        formHubListener.fetchFromServer();
    }
}
