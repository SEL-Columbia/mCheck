package org.who.mcheck.core.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.who.mcheck.core.domain.ReminderStatusToken;

import java.util.List;


@Repository
public class AllReminderStatusTokens extends MotechBaseRepository<ReminderStatusToken> {

    @Autowired
    public AllReminderStatusTokens(@Qualifier("mcheckDatabaseConnector") CouchDbConnector db) {
        super(ReminderStatusToken.class, db);
    }

    @GenerateView
    public ReminderStatusToken findByContactNumber(String contactNumber) {
        List<ReminderStatusToken> tokens = queryView("by_contactNumber", contactNumber);
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0);
    }

    public void addOrReplaceByPhoneNumber(ReminderStatusToken reminderStatusToken) {
        ReminderStatusToken token = findByContactNumber(reminderStatusToken.contactNumber());
        if (token != null) {
            remove(token);
        }
        add(reminderStatusToken);
    }
}
