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
    private MotherService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new MotherService(allMothers);
    }

    @Test
    public void shouldRegisterMother() throws Exception {
        MotherRegistrationRequest request = new MotherRegistrationRequest("id", "Anamika", "1234567890", "no", "no", "yes", "no", "yes", "no", "2013-01-01");

        service.registerMother(request);

        verify(allMothers).register(new Mother("id", "Anamika", "1234567890", "no", "no", "yes", "no", "yes", "no", "2013-01-01"));
    }

    @Test
    public void shouldFetchAllMothers() throws Exception {
        List<Mother> expectedMothers = asList(new Mother("id", "Anamika", "1234567890", "no", "yes", "yes", "no", "no", "yes", "2013-01-01"));
        when(allMothers.getAll()).thenReturn(expectedMothers);

        List<Mother> mothers = service.fetchAll();

        assertEquals(expectedMothers, mothers);
    }


}
