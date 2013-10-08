package org.who.mcheck.core.service;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreferredReminderTimeServiceTest {

    private PreferredReminderTimeService preferredReminderTimeService;

    @Before
    public void setUp() throws Exception {
        preferredReminderTimeService = new PreferredReminderTimeService("09:30:00", "14:30:00");
    }

    @Test
    public void shouldReturnMorningTimeForDailyCallPreferenceOfMorning() throws Exception {
        assertEquals(LocalTime.parse("09:30:00"), preferredReminderTimeService.getPreferredCallTime("morning"));
    }

    @Test
    public void shouldReturnAfterTimeForDailyCallPreferenceOfAfternoon() throws Exception {
        assertEquals(LocalTime.parse("14:30:00"), preferredReminderTimeService.getPreferredCallTime("afternoon"));
    }
}
