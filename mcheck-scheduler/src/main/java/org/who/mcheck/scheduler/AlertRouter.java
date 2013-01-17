package org.who.mcheck.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.motechproject.scheduletracking.api.events.constants.EventSubjects;
import org.springframework.stereotype.Component;

@Component
public class AlertRouter {
    private final Log log = LogFactory.getLog(AlertRouter.class);
    private AlertHandler handler;

    public void registerAsHandler(AlertHandler handler) {
        this.handler = handler;
    }

    @MotechListener(subjects = {EventSubjects.MILESTONE_ALERT})
    public void handle(MotechEvent realEvent) {
        log.info("Handling motech event : " + realEvent);
        MilestoneEvent event = new MilestoneEvent(realEvent);

        if (handler == null) {
            log.warn("No one is registered to whom events can be dispatched.");
            return;
        }

        handler.handleEvent(event);
    }
}
