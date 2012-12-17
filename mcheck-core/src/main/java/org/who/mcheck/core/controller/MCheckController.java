package org.who.mcheck.core.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.formhub.listener.FormHubFormSubmissionRouter;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.service.MotherService;

@Component
public class MCheckController {

    private final Log log = LogFactory.getLog(MCheckController.class);
    private final MotherService motherService;

    @Autowired
    public MCheckController(FormHubFormSubmissionRouter router, MotherService motherService) {
        this.motherService = motherService;
        router.registerForDispatch(this);
    }

    public void registerMother(MotherRegistrationRequest request) {
        log.info("Mother registration: " + request);

        motherService.registerMother(request);
    }
}
