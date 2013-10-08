package org.who.mcheck.core.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.decisiontree.core.model.INodeOperation;
import org.motechproject.decisiontree.server.domain.FlowSessionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.domain.ReminderStatusToken;
import org.who.mcheck.core.repository.AllReminderStatusTokens;

import static java.text.MessageFormat.format;

@Service
public class UpdateReminderStatusTokenOperation implements INodeOperation {

    private final Log log = LogFactory.getLog(MotherScheduleService.class);
    private AllReminderStatusTokens allReminderStatusTokens;

    private UpdateReminderStatusTokenOperation() {
    }

    @Autowired
    public UpdateReminderStatusTokenOperation(AllReminderStatusTokens allReminderStatusTokens) {
        this.allReminderStatusTokens = allReminderStatusTokens;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        ReminderStatusToken token = allReminderStatusTokens.findByContactNumber(session.getPhoneNumber());
        if (token == null) {
            log.warn(format("Could not find a call status token for the phone number: {0}. Flow session: {1}",
                    session.getPhoneNumber(), session));
            return;
        }
        log.warn(
                format("Found a call status for phone number: {0}, Updating the status to successful. Call detail: {1}",
                        session.getPhoneNumber(),
                        ToStringBuilder.reflectionToString(((FlowSessionRecord) session).getCallDetailRecord())));
        token.markCallStatusAsSuccessful();
        allReminderStatusTokens.update(token);
    }
}
