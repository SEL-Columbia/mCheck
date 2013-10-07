package org.who.mcheck.core.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.who.mcheck.core.domain.CallStatusToken;


@Repository
public class AllCallStatusTokens extends MotechBaseRepository<CallStatusToken> {

    @Autowired
    public AllCallStatusTokens(@Qualifier("mcheckDatabaseConnector") CouchDbConnector db) {
        super(CallStatusToken.class, db);
    }

    public CallStatusToken findByContactNumber(String contactNumber) {
        return null;
    }
}
