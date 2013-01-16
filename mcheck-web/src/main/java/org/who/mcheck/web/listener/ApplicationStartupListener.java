package org.who.mcheck.web.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.who.mcheck.core.service.CallTreeService;
import org.who.mcheck.core.service.ScheduleTrackingSeedService;
import org.who.mcheck.scheduler.MCheckScheduler;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
    public static final String APPLICATION_ID = "org.springframework.web.context.WebApplicationContext:/mcheck";
    private final MCheckScheduler mCheckScheduler;
    private final CallTreeService callTreeService;
    private ScheduleTrackingSeedService scheduleTrackingSeedService;

    @Autowired
    public ApplicationStartupListener(MCheckScheduler mCheckScheduler, CallTreeService callTreeService, ScheduleTrackingSeedService scheduleTrackingSeedService) {
        this.mCheckScheduler = mCheckScheduler;
        this.callTreeService = callTreeService;
        this.scheduleTrackingSeedService = scheduleTrackingSeedService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (APPLICATION_ID.equals(contextRefreshedEvent.getApplicationContext().getId())) {
            mCheckScheduler.startTimedScheduler();
            callTreeService.createMCheckIVRTrees();
            scheduleTrackingSeedService.createMCheckSchedules();
        }
    }
}
