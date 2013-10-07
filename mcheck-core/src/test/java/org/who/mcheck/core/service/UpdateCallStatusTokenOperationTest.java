package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.decisiontree.server.domain.FlowSessionRecord;
import org.who.mcheck.core.domain.CallStatus;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateCallStatusTokenOperationTest {

    @Mock
    private AllCallStatusTokens allCallStatusTokens;

    private UpdateCallStatusTokenOperation operation;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        operation = new UpdateCallStatusTokenOperation(allCallStatusTokens);
    }

    @Test
    public void shouldFindCallStatusByPhoneNumberAndUpdateTheStatusToSuccessWhenTheOperationIsPerformed() throws Exception {
        when(allCallStatusTokens.findByContactNumber("phone number 1")).thenReturn(new CallStatusToken("phone number 1", CallStatus.Unsuccessful));

        operation.perform(null, new FlowSessionRecord("sessionId", "phone number 1"));

        verify(allCallStatusTokens).update(new CallStatusToken("phone number 1", CallStatus.Successful));
    }
}
