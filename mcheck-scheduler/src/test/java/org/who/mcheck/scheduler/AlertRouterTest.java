package org.who.mcheck.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.api.events.MilestoneEvent;
import org.motechproject.scheduletracking.api.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.api.events.constants.EventSubjects;

import java.util.HashMap;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.who.formhub.api.util.EasyMap.mapOf;

public class AlertRouterTest {
    @Mock
    private AlertHandler handler;
    private AlertRouter router;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        router = new AlertRouter();
    }

    @Test
    public void shouldRouteMotechAlertEventToRegisteredHandler() throws Exception {
        MotechEvent realEvent = new MotechEvent(EventSubjects.MILESTONE_ALERT,
                new HashMap<String, Object>(mapOf(EventDataKeys.EXTERNAL_ID, "mother id")));

        router.registerAsHandler(handler);
        router.handle(realEvent);

        verify(handler).handleEvent(assertMilestoneEvent());
    }

    private MilestoneEvent assertMilestoneEvent() {
        return argThat(new ArgumentMatcher<MilestoneEvent>() {
            @Override
            public boolean matches(Object o) {
                MilestoneEvent event = (MilestoneEvent) o;
                return event.getExternalId().equals("mother id");
            }
        });
    }
}
