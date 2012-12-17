package org.who.mcheck.core.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.who.mcheck.core.domain.Mother;


@Repository
public class AllMothers extends MotechBaseRepository<Mother> {

    @Autowired
    public AllMothers(@Qualifier("mcheckDatabaseConnector") CouchDbConnector db) {
        super(Mother.class, db);
    }

    public void register(Mother mother) {
        add(mother);
    }
}
