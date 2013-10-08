package org.who.mcheck.core.service;

import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.AllConstants;

@Service
public class PreferredReminderTimeService {

    private final String preferredCallTimeInMorning;
    private final String preferredCallTimeInAfternoon;

    @Autowired
    public PreferredReminderTimeService(
            @Value("#{mCheck['ivr.preferred.call.time.morning']}") String preferredCallTimeInMorning,
            @Value("#{mCheck['ivr.preferred.call.time.afternoon']}") String preferredCallTimeInAfternoon) {
        this.preferredCallTimeInMorning = preferredCallTimeInMorning;
        this.preferredCallTimeInAfternoon = preferredCallTimeInAfternoon;
    }

    public LocalTime getPreferredCallTime(String dailyCallPreference) {
        String preferredCallTime =
                AllConstants.Schedule.MORNING.equalsIgnoreCase(dailyCallPreference)
                        ? preferredCallTimeInMorning
                        : preferredCallTimeInAfternoon;
        return LocalTime.parse(preferredCallTime);
    }
}
