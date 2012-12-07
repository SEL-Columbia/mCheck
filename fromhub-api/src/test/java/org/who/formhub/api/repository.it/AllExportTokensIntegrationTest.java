package org.who.formhub.api.repository.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.who.formhub.api.domain.ExportToken;
import org.who.formhub.api.repository.AllExportTokens;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-formhub-api.xml")
public class AllExportTokensIntegrationTest {
    @Autowired
    private AllExportTokens allExportTokens;

    @Before
    public void setUp() throws Exception {
        allExportTokens.removeAll();
    }

    @Test
    public void shouldCreateATokenWhenATokenDoesNotExist() {
        allExportTokens.updateToken("formNameForTokenWhichDoesNotExistYet", "SOME-DATA-HERE");

        ExportToken token = allExportTokens.findByFormName("formNameForTokenWhichDoesNotExistYet");
        assertThat(token.value(), is("SOME-DATA-HERE"));
        assertThat(allExportTokens.get(token.getId()).value(), is("SOME-DATA-HERE"));
    }

    @Test
    public void shouldUpdateATokenWhichExists() {
        String formName = "formNameForToken";

        allExportTokens.updateToken(formName, "SOME-VALUE-HERE");
        assertThat(allExportTokens.findByFormName(formName).value(), is("SOME-VALUE-HERE"));

        allExportTokens.updateToken(formName, "NEW-VALUE-HERE");
        assertThat(allExportTokens.findByFormName(formName).value(), is("NEW-VALUE-HERE"));
    }
}
