package org.who.mcheck.core.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.formhub.listener.FormHubFormSubmissionRouter;
import org.who.mcheck.core.domain.MotherRegistrationRequest;
import org.who.mcheck.core.service.MotherService;

@Component
public class MCheckController {

    private final Log log = LogFactory.getLog(MCheckController.class);
    private final FormHubFormSubmissionRouter router;
    private final MotherService motherService;

    @Autowired
    public MCheckController(FormHubFormSubmissionRouter router, MotherService motherService) {
        this.router = router;
        this.motherService = motherService;
        this.router.registerForDispatch(this);
    }

    public void registerMother(MotherRegistrationRequest motherRegistrationRequest) {
        log.info("Registering mother: " + motherRegistrationRequest);
    }
}
