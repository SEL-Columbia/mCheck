package org.who.mcheck.core.util;

import org.joda.time.LocalTime;

public class LocalTimeUtil {
    private static LocalTimeUtility timeUtility = new RealLocalTime();

    public static void fakeIt(LocalTime fakeTime) {
        timeUtility = new MockLocalTime(fakeTime);
    }

    public static LocalTime now() {
        return timeUtility.now();
    }
}

interface LocalTimeUtility {
    LocalTime now();
}

class RealLocalTime implements LocalTimeUtility {

    @Override
    public LocalTime now() {
        return LocalTime.now();
    }
}

class MockLocalTime implements LocalTimeUtility {
    private LocalTime fakeTime;

    public MockLocalTime(LocalTime fakeTime) {
        this.fakeTime = fakeTime;
    }

    @Override
    public LocalTime now() {
        return fakeTime;
    }
}
