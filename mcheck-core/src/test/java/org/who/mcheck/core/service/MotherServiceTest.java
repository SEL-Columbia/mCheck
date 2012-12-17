package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import static org.mockito.Mockito.verify;
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
        MotherRegistrationRequest request = new MotherRegistrationRequest("id", "Anamika", "1234567890", "no", "2013-01-01");

        service.registerMother(request);

        verify(allMothers).register(new Mother("id", "Anamika", "1234567890", "no", "2013-01-01"));
    }
}
