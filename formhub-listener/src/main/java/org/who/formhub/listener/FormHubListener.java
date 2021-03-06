package org.who.formhub.listener;

import org.motechproject.event.OutboundEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.formhub.api.contract.FormHubFormInstance;
import org.who.formhub.api.service.FormHubImportService;
import org.who.formhub.listener.event.FormHubFormEvent;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class FormHubListener {
    private FormHubImportService formHubImportService;
    private OutboundEventGateway outboundEventGateway;
    private static final ReentrantLock lock = new ReentrantLock();
    private static Logger logger = LoggerFactory.getLogger(FormHubListener.class);

    @Autowired
    public FormHubListener(FormHubImportService formHubImportService, OutboundEventGateway outboundEventGateway) {
        this.formHubImportService = formHubImportService;
        this.outboundEventGateway = outboundEventGateway;
    }

    public void fetchFromServer() {
        if (!lock.tryLock()) {
            logger.warn("Not fetching from FormHub. It is already in progress.");
            return;
        }
        try {
            logger.info("Fetching from FormHub.");
            List<FormHubFormInstance> formInstances = formHubImportService.fetchForms();
            outboundEventGateway.sendEventMessage(new FormHubFormEvent(formInstances).toMotechEvent());
            logger.info("Done fetching from FormHub.");
        } finally {
            lock.unlock();
        }
    }
}
