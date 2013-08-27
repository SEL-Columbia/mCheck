package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReminderServiceTest {
    @Mock
    private AllMothers allMothers;

    @Mock
    private IVRService ivrService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldInitiateACallToRemindMother() throws Exception {
        Mother mother = new Mother("id", "Anamika", "Arun", "caseId",
                "2013-01-01", "2013-01-01", "1234567890", "morning", "instanceId", "2013-01-01");
        when(allMothers.motherExists("mother id")).thenReturn(true);
        when(allMothers.get("mother id")).thenReturn(mother);

        ReminderService reminderService = new ReminderService(allMothers, ivrService, "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-{0}&trP=Lw&ln=en");
        reminderService.remindMother("mother id", "Day 4");

        verify(ivrService).initiateCall(assertCallRequest(mother.contactNumber(), "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-Day 4&trP=Lw&ln=en"));
    }

    @Test
    public void shouldNotInitiateACallToRemindMotherWhenMotherNotFound() throws Exception {
        when(allMothers.motherExists("mother id")).thenReturn(false);

        ReminderService reminderService = new ReminderService(allMothers, ivrService, "http://server.com/mcheckivr/kookoo/ivr?tree=mCheckTree-{0}&trP=Lw&ln=en");
        reminderService.remindMother("mother id", "Day 1");

        verifyZeroInteractions(ivrService);
    }

    private CallRequest assertCallRequest(final String phoneNumber, final String callbackUrl) {
        return argThat(new ArgumentMatcher<CallRequest>() {
            @Override
            public boolean matches(Object o) {
                CallRequest callRequest = (CallRequest) o;
                return phoneNumber.equals(callRequest.getPhone()) && callbackUrl.equals(callRequest.getCallBackUrl());
            }
        });
    }

}
