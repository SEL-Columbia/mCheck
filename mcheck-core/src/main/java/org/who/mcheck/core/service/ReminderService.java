package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.ivr.service.CallRequest;
import org.motechproject.ivr.service.IVRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import java.util.HashMap;

@Service
public class ReminderService {

    private final AllMothers allMothers;
    private final IVRService ivrService;
    private final String callbackUrl;
    private final Log log = LogFactory.getLog(ReminderService.class);

    @Autowired
    public ReminderService(AllMothers allMothers,
                           IVRService ivrService,
                           @Value("#{mCheck['ivr.callback.url']}") String callbackUrl) {
        this.allMothers = allMothers;
        this.ivrService = ivrService;
        this.callbackUrl = callbackUrl;
    }

    public void remindMother(String motherId) {
        if (!allMothers.motherExists(motherId)) {
            log.warn("Got alert for a non-registered mother for ID: " + motherId);
            return;
        }

        Mother mother = allMothers.get(motherId);

        log.info("Calling mother: " + mother + ". Call back URL: " + callbackUrl);
        CallRequest callRequest = new CallRequest(mother.contactNumber(), new HashMap<String, String>(), callbackUrl);
        ivrService.initiateCall(callRequest);
    }
}
