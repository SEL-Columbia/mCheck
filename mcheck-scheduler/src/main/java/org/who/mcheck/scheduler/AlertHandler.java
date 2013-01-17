package org.who.mcheck.scheduler;

import org.motechproject.scheduletracking.api.events.MilestoneEvent;

public interface AlertHandler {
    void handleEvent(MilestoneEvent event);
}
