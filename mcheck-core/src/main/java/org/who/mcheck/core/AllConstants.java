package org.who.mcheck.core;

import org.joda.time.LocalTime;
import org.motechproject.model.Time;

public class AllConstants {

    public static class Schedule {
        public static final String POST_PREGNANCY_DANGER_SIGNS_SCHEDULE_NAME = "Post Pregnancy Danger Signs";
        public static final Time PREFERRED_ALERT_TIME = new Time(new LocalTime(14, 0));
    }

}
