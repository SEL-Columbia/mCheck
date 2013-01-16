package org.who.mcheck.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.who.mcheck.core.contract.MotherRegistrationRequest;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import java.util.List;

@Service
public class MotherService {
    private final AllMothers allMothers;
    private MotherScheduleService scheduleService;

    @Autowired
    public MotherService(AllMothers allMothers, MotherScheduleService scheduleService) {
        this.allMothers = allMothers;
        this.scheduleService = scheduleService;
    }

    public void registerMother(MotherRegistrationRequest request) {
        Mother mother = new Mother(
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
        );
        allMothers.register(mother);
        scheduleService.enroll(mother.getId(), mother.registrationDate());
    }

    public List<Mother> fetchAll() {
        return allMothers.getAll();
    }
}
