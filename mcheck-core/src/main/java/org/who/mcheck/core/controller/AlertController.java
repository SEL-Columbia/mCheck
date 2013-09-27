package org.who.mcheck.core.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.mcheck.core.service.ReminderService;
import org.who.mcheck.scheduler.AlertHandler;
import org.who.mcheck.scheduler.AlertRouter;

import static org.who.mcheck.core.AllConstants.Schedule.POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME_PREFIX;

@Component
public class AlertController implements AlertHandler {
    private Log log = LogFactory.getLog(AlertController.class);

    private ReminderService reminderService;

    @Autowired
    public AlertController(AlertRouter router, ReminderService reminderService) {
        this.reminderService = reminderService;
        router.registerAsHandler(this);
    }

    @Override
    public void handleEvent(MilestoneEvent event) {
        if (!StringUtils.contains(event.getScheduleName(), POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME_PREFIX)) {
            log.warn("Got alert for an unknown schedule. Event is : " + event);
            return;
        }

        reminderService.remindMother(event.getExternalId(), event.getMilestoneAlert().getMilestoneName());
    }
}
