package org.who.mcheck.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import java.util.List;

@Component
public class MotherService {
    private final AllMothers allMothers;

    @Autowired
    public MotherService(AllMothers allMothers) {
        this.allMothers = allMothers;
    }

    public void registerMother(MotherRegistrationRequest request) {
        allMothers.register(
                new Mother(
                        request.formHubId(),
                        request.name(),
                        request.contactNumber(),
                        request.hasBleeding(),
                        request.hasFever(),
                        request.hasPainfulUrination(),
                        request.hasVaginalDischarge(),
                        request.hasHeadache(),
                        request.hasProblemBreathing(),
                        request.submissionDate()
                )
        );
    }

    public List<Mother> fetchAll() {
        return allMothers.getAll();
    }
}
