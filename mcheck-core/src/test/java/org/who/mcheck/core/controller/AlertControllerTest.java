package org.who.mcheck.core.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.motechproject.scheduletracking.api.events.constants.EventDataKeys;
import org.who.mcheck.core.service.ReminderService;
import org.who.mcheck.scheduler.AlertRouter;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.scheduletracking.api.events.constants.EventDataKeys.EXTERNAL_ID;
import static org.motechproject.scheduletracking.api.events.constants.EventSubjects.MILESTONE_ALERT;
import static org.who.formhub.api.util.EasyMap.create;

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
        MilestoneEvent event = getMilestoneEvent("mother id", "Post Pregnancy Danger Signs");

        controller.handleEvent(event);

        verify(reminderService).remindMother("mother id");
    }

    @Test
    public void shouldNotDelegateAlertHandlingToReminderServiceWhenAlertIsNotForPostPregnancyDangerSignsSchedule() throws Exception {
        MilestoneEvent event = getMilestoneEvent("mother id", "unknown schedule");

        controller.handleEvent(event);

        verifyZeroInteractions(reminderService);
    }

    private MilestoneEvent getMilestoneEvent(String externalId, String scheduleName) {
        return new MilestoneEvent(new MotechEvent(MILESTONE_ALERT,
                new HashMap<String, Object>(
                        create(EXTERNAL_ID, externalId)
                                .put(EventDataKeys.SCHEDULE_NAME, scheduleName)
                                .map())));
    }
}
