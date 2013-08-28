package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotherServiceTest {
    @Mock
    private AllMothers allMothers;
    @Mock
    private MotherScheduleService scheduleService;
    private MotherService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new MotherService(allMothers, scheduleService);
    }

    @Test
    public void shouldRegisterMother() throws Exception {
        MotherRegistrationRequest request = new MotherRegistrationRequest("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");

        service.registerMother(request);

        verify(allMothers).register(new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01"));
    }

    @Test
    public void shouldEnrollMotherToSchedulesWhenSheIsRegistered() throws Exception {
        MotherRegistrationRequest request = new MotherRegistrationRequest("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");

        service.registerMother(request);

        verify(scheduleService).enroll(null, "2013-01-01", "morning");
    }

    @Test
    public void shouldFetchAllMothers() throws Exception {
        List<Mother> expectedMothers = asList(new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01"));
        when(allMothers.getAll()).thenReturn(expectedMothers);

        List<Mother> mothers = service.fetchAll();

        assertEquals(expectedMothers, mothers);
    }
}
