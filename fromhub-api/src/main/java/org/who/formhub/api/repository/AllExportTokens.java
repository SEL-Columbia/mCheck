package org.who.formhub.api.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.who.formhub.api.domain.ExportToken;

import java.util.List;

@Repository
public class AllExportTokens extends MotechBaseRepository<ExportToken> {
    @Autowired
    protected AllExportTokens(@Qualifier("formHubDatabaseConnector") CouchDbConnector db) {
        super(ExportToken.class, db);
    }

    @GenerateView
    public ExportToken findByFormName(String formName) {
        List<ExportToken> tokens = queryView("by_formName", formName);
        if (tokens == null || tokens.isEmpty()) {
            return new ExportToken(formName, "");
        }
        return tokens.get(0);
    }

    public void updateToken(String formName, String newToken) {
        addOrReplace(new ExportToken(formName, newToken), "formName", formName);
    }
}
