package org.who.mcheck.core.service;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreferredCallTimeServiceTest {

    private PreferredCallTimeService preferredCallTimeService;

    @Before
    public void setUp() throws Exception {
        preferredCallTimeService = new PreferredCallTimeService("09:30:00", "14:30:00");
    }

    @Test
    public void shouldReturnMorningTimeForDailyCallPreferenceOfMorning() throws Exception {
        assertEquals(LocalTime.parse("09:30:00"), preferredCallTimeService.getPreferredCallTime("morning"));
    }

    @Test
    public void shouldReturnAfterTimeForDailyCallPreferenceOfAfternoon() throws Exception {
        assertEquals(LocalTime.parse("14:30:00"), preferredCallTimeService.getPreferredCallTime("afternoon"));
    }
}
