package org.who.mcheck.core.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.who.mcheck.core.domain.CallStatusToken;

import java.util.List;


@Repository
public class AllCallStatusTokens extends MotechBaseRepository<CallStatusToken> {

    @Autowired
    public AllCallStatusTokens(@Qualifier("mcheckDatabaseConnector") CouchDbConnector db) {
        super(CallStatusToken.class, db);
    }

    @GenerateView
    public CallStatusToken findByContactNumber(String contactNumber) {
        List<CallStatusToken> tokens = queryView("by_contactNumber", contactNumber);
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0);
    }

    public void addOrReplaceByPhoneNumber(CallStatusToken callStatusToken) {
        CallStatusToken token = findByContactNumber(callStatusToken.contactNumber());
        if (token != null) {
            remove(token);
        }
        add(callStatusToken);
    }
}
