package org.who.mcheck.core;

public class AllConstants {

    public static final int DEFAULT_FIRST_CALL_DELAY_IN_MINUTES = 5;
    public static final int DEFAULT_VALUE_FOR_RETRY_INTERVAL = 20;
    public static final String RETRY_CALL_EVENT_SUBJECT = "RETRY-CALL-EVENT";

    public static class BirthRegistrationFormFields {
        public static final String CONTACT_NUMBER = "contactNumber";
    }

    public static class Schedule {
        public static final String POST_DELIVERY_DANGER_SIGNS_SCHEDULE_NAME_PREFIX = "Post Delivery Danger Signs";
        public static final String MORNING = "morning";
        public static final String POST_DELIVERY_DANGER_SIGNS_SCHEDULE_TEMPLATE = "Post Delivery Danger Signs - Day {0}";
        public static final int LAST_DAY_OF_POST_DELIVERY_DANGER_SIGNS_SCHEDULE = 7;
    }

}
