package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.server.domain.FlowSessionRecord;
import org.who.mcheck.core.domain.ReminderStatus;
import org.who.mcheck.core.domain.ReminderStatusToken;
import org.who.mcheck.core.repository.AllReminderStatusTokens;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateReminderStatusTokenOperationTest {

    @Mock
    private AllReminderStatusTokens allReminderStatusTokens;

    private UpdateReminderStatusTokenOperation operation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        operation = new UpdateReminderStatusTokenOperation(allReminderStatusTokens);
    }

    @Test
    public void shouldFindCallStatusByPhoneNumberAndUpdateTheStatusToSuccessWhenTheOperationIsPerformed() throws Exception {
        when(allReminderStatusTokens.findByContactNumber("phone number 1")).thenReturn(new ReminderStatusToken("phone number 1", ReminderStatus.Unsuccessful));

        operation.perform(null, new FlowSessionRecord("sessionId", "phone number 1"));

        verify(allReminderStatusTokens).update(new ReminderStatusToken("phone number 1", ReminderStatus.Successful));
    }
}
