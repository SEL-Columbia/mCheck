package org.who.mcheck.core.repository.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.who.mcheck.core.domain.ReminderStatus;
import org.who.mcheck.core.domain.ReminderStatusToken;
import org.who.mcheck.core.repository.AllReminderStatusTokens;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-mcheck.xml")
public class AllReminderStatusTokensTest {

    @Autowired
    private AllReminderStatusTokens allReminderStatusTokens;

    @Before
    public void setUp() throws Exception {
        allReminderStatusTokens.removeAll();
    }

    @Test
    public void shouldFindCallStatusByPhoneNumber() throws Exception {
        ReminderStatusToken expectedTokenForPhone1 = new ReminderStatusToken("phone 1", ReminderStatus.Unsuccessful);
        ReminderStatusToken expectedTokenForPhone2 = new ReminderStatusToken("phone 2", ReminderStatus.Unsuccessful);
        allReminderStatusTokens.add(expectedTokenForPhone1);
        allReminderStatusTokens.add(expectedTokenForPhone2);

        ReminderStatusToken tokenForPhone1 = allReminderStatusTokens.findByContactNumber("phone 1");
        assertEquals(expectedTokenForPhone1, tokenForPhone1);

        ReminderStatusToken tokenForPhone2 = allReminderStatusTokens.findByContactNumber("phone 2");
        assertEquals(expectedTokenForPhone2, tokenForPhone2);
    }

    @Test
    public void shouldUpdateCallStatusByPhoneNumber() throws Exception {
        allReminderStatusTokens.add(new ReminderStatusToken("phone 1", ReminderStatus.Unsuccessful));

        allReminderStatusTokens.addOrReplaceByPhoneNumber(new ReminderStatusToken("phone 1", ReminderStatus.Successful));

        ReminderStatusToken updatedToken = allReminderStatusTokens.findByContactNumber("phone 1");
        assertEquals(new ReminderStatusToken("phone 1", ReminderStatus.Successful), updatedToken);
    }

    @Test
    public void shouldCreateCallStatusWhenItDoesNotExistByPhoneNumber() throws Exception {
        allReminderStatusTokens.addOrReplaceByPhoneNumber(new ReminderStatusToken("phone 1", ReminderStatus.Successful));

        ReminderStatusToken createdToken = allReminderStatusTokens.findByContactNumber("phone 1");
        assertEquals(new ReminderStatusToken("phone 1", ReminderStatus.Successful), createdToken);
    }
}
