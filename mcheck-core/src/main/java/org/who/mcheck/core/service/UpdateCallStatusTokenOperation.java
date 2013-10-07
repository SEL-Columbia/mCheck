package org.who.mcheck.core.service;

import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.domain.CallStatusToken;
import org.who.mcheck.core.repository.AllCallStatusTokens;

@Service
public class UpdateCallStatusTokenOperation implements INodeOperation {

    private AllCallStatusTokens allCallStatusTokens;

    @Autowired
    public UpdateCallStatusTokenOperation(AllCallStatusTokens allCallStatusTokens) {
        this.allCallStatusTokens = allCallStatusTokens;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        CallStatusToken token = allCallStatusTokens.findByContactNumber(session.getPhoneNumber());
        token.markCallStatusAsSuccessful();
        allCallStatusTokens.update(token);
    }
}
