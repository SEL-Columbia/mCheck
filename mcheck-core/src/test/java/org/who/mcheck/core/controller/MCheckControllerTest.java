package org.who.mcheck.core.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.who.formhub.listener.FormHubFormSubmissionRouter;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.service.MotherService;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MCheckControllerTest {
    @Mock
    FormHubFormSubmissionRouter router;

    @Mock
    MotherService motherService;
    private MCheckController controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = new MCheckController(router, motherService);
    }

    @Test
    public void shouldDelegateMotherRegistrationToMotherService() throws Exception {
        MotherRegistrationRequest request = new MotherRegistrationRequest("id", "Anamika", "Arun", "caseId", "2013-01-01", "2013-01-01", "1234567890",
                "morning", "instanceId", "2013-01-01"
        );

        controller.registerMother(request);

        verify(motherService).registerMother(request);
    }
}
