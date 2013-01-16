package org.who.mcheck.core.service;

import org.motechproject.scheduletracking.api.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.motechproject.scheduletracking.api.repository.TrackedSchedulesJsonReader;
import org.motechproject.scheduletracking.api.repository.TrackedSchedulesJsonReaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTrackingSeedService {
    private AllSchedules allSchedules;
    private String schedulesDirectory;
    private TrackedSchedulesJsonReader trackedSchedulesJsonReader;

    @Autowired
    public ScheduleTrackingSeedService(AllSchedules allSchedules,
                                       @Value("#{schedule_tracking['schedule.definitions.directory']}") String schedulesDirectory) {
        this.allSchedules = allSchedules;
        this.schedulesDirectory = schedulesDirectory;
        this.trackedSchedulesJsonReader = new TrackedSchedulesJsonReaderImpl();
    }

    public void createMCheckSchedules() {
        allSchedules.removeAll();
        List<ScheduleRecord> scheduleRecords = trackedSchedulesJsonReader.getAllSchedules(schedulesDirectory);
        for (ScheduleRecord scheduleRecord : scheduleRecords) {
            allSchedules.add(scheduleRecord);
        }
    }
}
