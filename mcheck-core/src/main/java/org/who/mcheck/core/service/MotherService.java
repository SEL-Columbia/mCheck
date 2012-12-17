package org.who.mcheck.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

@Component
public class MotherService {
    private final AllMothers allMothers;

    @Autowired
    public MotherService(AllMothers allMothers) {
        this.allMothers = allMothers;
    }

    public void registerMother(MotherRegistrationRequest request) {
        allMothers.register(new Mother(request.formHubId(), request.name(), request.contactNumber(), request.bleeding(), request.submissionDate()));
    }
}
