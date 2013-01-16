package org.who.mcheck.core.service;

import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.scheduletracking.api.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.motechproject.scheduletracking.api.repository.TrackedSchedulesJsonReader;
import org.motechproject.scheduletracking.api.repository.TrackedSchedulesJsonReaderImpl;

import java.lang.reflect.Type;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScheduleTrackingSeedServiceTest {
    @Mock
    private AllSchedules allSchedules;
    private ScheduleTrackingSeedService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new ScheduleTrackingSeedService(allSchedules, "/schedules");

    }

    @Test
    public void shouldCreateScheduleRecords() throws Exception {
        Type type = new TypeToken<ScheduleRecord>() { } .getType();
        ScheduleRecord schedule = (ScheduleRecord) new MotechJsonReader().readFromFile("/schedules/simple-schedule.json", type);
        ScheduleRecord anotherSchedule = (ScheduleRecord) new MotechJsonReader().readFromFile("/schedules/another-simple-schedule.json", type);

        service.createMCheckSchedules();

        verify(allSchedules).removeAll();
        verify(allSchedules).add(schedule);
        verify(allSchedules).add(anotherSchedule);

    }
}
