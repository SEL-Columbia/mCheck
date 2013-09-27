package org.who.mcheck.core.controller;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduletracking.api.domain.Milestone;
import org.motechproject.scheduletracking.api.domain.MilestoneAlert;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.who.mcheck.core.service.ReminderService;
import org.who.mcheck.scheduler.AlertRouter;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AlertControllerTest {
    @Mock
    private AlertRouter alertRouter;
    @Mock
    private ReminderService reminderService;
    private AlertController controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = new AlertController(alertRouter, reminderService);
    }

    @Test
    public void shouldRegisterItselfAsAlertHandlerWithRouter() throws Exception {
        AlertController alertController = new AlertController(alertRouter, reminderService);

        verify(alertRouter).registerAsHandler(alertController);
    }

    @Test
    public void shouldDelegateAlertHandlingToReminderService() throws Exception {
        MilestoneEvent event = getMilestoneEvent("mother id", "Post Delivery Danger Signs - Day 1", "Day 2");

        controller.handleEvent(event);

        verify(reminderService).remindMother("mother id", "Post Delivery Danger Signs - Day 1", "Day 2");
    }

    @Test
    public void shouldNotDelegateAlertHandlingToReminderServiceWhenAlertIsNotForPostPregnancyDangerSignsSchedule() throws Exception {
        MilestoneEvent event = getMilestoneEvent("mother id", "unknown schedule", null);

        controller.handleEvent(event);

        verifyZeroInteractions(reminderService);
    }

    private MilestoneEvent getMilestoneEvent(String externalId, String scheduleName, String milestoneName) {
        Milestone milestone = mock(Milestone.class);
        when(milestone.getName()).thenReturn(milestoneName);
        MilestoneAlert milestoneAlert = MilestoneAlert.fromMilestone(milestone, DateTime.now());

        MilestoneEvent milestoneEvent = mock(MilestoneEvent.class);
        when(milestoneEvent.getExternalId()).thenReturn(externalId);
        when(milestoneEvent.getScheduleName()).thenReturn(scheduleName);
        when(milestoneEvent.getMilestoneAlert()).thenReturn(milestoneAlert);

        return milestoneEvent;
    }
}
