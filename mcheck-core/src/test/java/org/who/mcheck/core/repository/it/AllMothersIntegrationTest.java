package org.who.mcheck.core.repository.it;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.who.mcheck.core.domain.Mother;
import org.who.mcheck.core.repository.AllMothers;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-mcheck.xml")
public class AllMothersIntegrationTest {
    @Autowired
    private AllMothers allMothers;

    @Before
    public void setUp() throws Exception {
        allMothers.removeAll();
    }

    @Test
    public void shouldRegisterAMother() {
        Mother mother = new Mother("id", "Anamika", "1234567890", "no", "2013-01-01");

        allMothers.register(mother);

        List<Mother> allTheMothers = allMothers.getAll();
        assertTrue(allTheMothers.contains(mother));
    }
}